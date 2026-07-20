// service/impl/PaymentServiceImpl.java
package com.medislot.coreservice.service.impl;

import com.medislot.coreservice.dto.request.CreatePaymentRequest;
import com.medislot.coreservice.dto.request.VerifyPaymentRequest;
import com.medislot.coreservice.dto.response.CreatePaymentResponse;
import com.medislot.coreservice.dto.response.PaymentResponse;
import com.medislot.coreservice.entity.Appointment;
import com.medislot.coreservice.entity.Payment;
import com.medislot.coreservice.entity.PatientProfile;
import com.medislot.coreservice.entity.User;
import com.medislot.coreservice.enums.AppointmentStatus;
import com.medislot.coreservice.enums.PaymentStatus;
import com.medislot.coreservice.exception.*;
import com.medislot.coreservice.mapper.PaymentMapper;
import com.medislot.coreservice.repository.AppointmentRepository;
import com.medislot.coreservice.repository.PatientProfileRepository;
import com.medislot.coreservice.repository.PaymentRepository;
import com.medislot.coreservice.security.CurrentUserProvider;
import com.medislot.coreservice.service.PaymentService;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final AppointmentRepository appointmentRepository;
    private final PatientProfileRepository patientProfileRepository;
    private final PaymentMapper paymentMapper;
    private final CurrentUserProvider currentUserProvider;
    private final RazorpayClient razorpayClient;

    @Value("${ms.razorpay.key-id}")
    private String razorpayKeyId;

    @Value("${ms.razorpay.key-secret}")
    private String razorpayKeySecret;

    @Value("${ms.razorpay.webhook-secret}")
    private String razorpayWebhookSecret;

    @Override
    @Transactional
    public CreatePaymentResponse createOrder(CreatePaymentRequest request) {
        PatientProfile patientProfile = getCurrentPatientProfile();

        Appointment appointment = appointmentRepository.findById(request.getAppointmentId())
                .orElseThrow(() -> new AppointmentNotFoundException(
                        "Appointment not found with id: " + request.getAppointmentId()));

        if (!appointment.getPatientProfile().getId().equals(patientProfile.getId())) {
            throw new AppointmentNotFoundException("Appointment not found with id: " + request.getAppointmentId());
        }

        if (appointment.getAppointmentStatus() != AppointmentStatus.PENDING_PAYMENT) {
            throw new PaymentAlreadyProcessedException("Payment already completed for this appointment");
        }

        Double fee = appointment.getDoctorProfile().getConsultationFee();

        try {
            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", Math.round(fee * 100));
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", "appt_rcpt_" + appointment.getId());
            orderRequest.put("payment_capture", 1);

            Order order = razorpayClient.orders.create(orderRequest);
            String razorpayOrderId = order.get("id");

            Payment payment = paymentRepository.findByAppointmentId(appointment.getId())
                    .orElse(Payment.builder().appointment(appointment).build());

            payment.setRazorpayOrderId(razorpayOrderId);
            payment.setAmount(fee);
            payment.setCurrency("INR");
            payment.setPaymentStatus(PaymentStatus.PENDING);
            paymentRepository.save(payment);

            return CreatePaymentResponse.builder()
                    .razorpayOrderId(razorpayOrderId)
                    .razorpayKeyId(razorpayKeyId)
                    .amount(fee)
                    .currency("INR")
                    .appointmentId(appointment.getId())
                    .build();

        } catch (RazorpayException ex) {
            throw new RuntimeException("Failed to create Razorpay order: " + ex.getMessage());
        }
    }

    @Override
    @Transactional
    public PaymentResponse verifyPayment(VerifyPaymentRequest request) {
        PatientProfile patientProfile = getCurrentPatientProfile();

        Payment payment = paymentRepository.findByRazorpayOrderId(request.getRazorpayOrderId())
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found for this order"));

        Appointment appointment = payment.getAppointment();

        if (!appointment.getPatientProfile().getId().equals(patientProfile.getId())) {
            throw new PaymentNotFoundException("Payment not found for this order");
        }

        if (payment.getPaymentStatus() == PaymentStatus.SUCCESS) {
            throw new PaymentAlreadyProcessedException("Payment already verified for this appointment");
        }

        JSONObject attributes = new JSONObject();
        attributes.put("razorpay_order_id", request.getRazorpayOrderId());
        attributes.put("razorpay_payment_id", request.getRazorpayPaymentId());
        attributes.put("razorpay_signature", request.getRazorpaySignature());

        boolean isValid;
        try {
            isValid = Utils.verifyPaymentSignature(attributes, razorpayKeySecret);
        } catch (RazorpayException ex) {
            isValid = false;
        }

        if (!isValid) {
            payment.setPaymentStatus(PaymentStatus.FAILED);
            paymentRepository.save(payment);
            throw new InvalidPaymentException("Payment signature verification failed");
        }

        payment.setRazorpayPaymentId(request.getRazorpayPaymentId());
        payment.setRazorpaySignature(request.getRazorpaySignature());
        payment.setPaymentStatus(PaymentStatus.SUCCESS);
        paymentRepository.save(payment);

        appointment.setAppointmentStatus(AppointmentStatus.CONFIRMED);
        appointmentRepository.save(appointment);

        return paymentMapper.toResponse(payment);
    }

    @Override
    @Transactional
    public void processWebhookEvent(String payload, String signatureHeader) {
        boolean valid;
        try {
            valid = Utils.verifyWebhookSignature(payload, signatureHeader, razorpayWebhookSecret);
        } catch (RazorpayException ex) {
            valid = false;
        }

        if (!valid) {
            throw new InvalidPaymentException("Invalid webhook signature");
        }

        JSONObject json = new JSONObject(payload);
        String event = json.getString("event");

        if (!"payment.captured".equals(event)) {
            return;
        }

        JSONObject paymentEntity = json.getJSONObject("payload").getJSONObject("payment").getJSONObject("entity");
        String razorpayOrderId = paymentEntity.getString("order_id");
        String razorpayPaymentId = paymentEntity.getString("id");

        Payment payment = paymentRepository.findByRazorpayOrderId(razorpayOrderId).orElse(null);
        if (payment == null || payment.getPaymentStatus() == PaymentStatus.SUCCESS) {
            return; // unknown order, or already confirmed — idempotent no-op
        }

        payment.setPaymentStatus(PaymentStatus.SUCCESS);
        payment.setRazorpayPaymentId(razorpayPaymentId);
        paymentRepository.save(payment);

        Appointment appointment = payment.getAppointment();
        appointment.setAppointmentStatus(AppointmentStatus.CONFIRMED);
        appointmentRepository.save(appointment);
    }

    private PatientProfile getCurrentPatientProfile() {
        User currentUser = currentUserProvider.getCurrentUser();
        return patientProfileRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> new ProfileNotFoundException("Patient profile not found. Please create one first."));
    }
}
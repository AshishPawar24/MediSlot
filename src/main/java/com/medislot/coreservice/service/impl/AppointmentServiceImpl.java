package com.medislot.coreservice.service.impl;

import com.medislot.coreservice.dto.request.AppointmentBookingRequest;
import com.medislot.coreservice.dto.request.UpdateAppointmentStatusRequest;
import com.medislot.coreservice.dto.response.AppointmentResponse;
import com.medislot.coreservice.dto.response.AppointmentStatusResponse;
import com.medislot.coreservice.dto.response.DoctorAppointmentResponse;
import com.medislot.coreservice.dto.response.PatientAppointmentResponse;
import com.medislot.coreservice.entity.Appointment;
import com.medislot.coreservice.entity.AppointmentSlot;
import com.medislot.coreservice.entity.DoctorProfile;
import com.medislot.coreservice.entity.PatientProfile;
import com.medislot.coreservice.entity.User;
import com.medislot.coreservice.enums.AppointmentStatus;
import com.medislot.coreservice.enums.SlotStatus;
import com.medislot.coreservice.exception.AppointmentNotFoundException;
import com.medislot.coreservice.exception.InvalidAppointmentStatusException;
import com.medislot.coreservice.exception.ProfileNotFoundException;
import com.medislot.coreservice.exception.SlotAlreadyBookedException;
import com.medislot.coreservice.exception.SlotNotFoundException;
import com.medislot.coreservice.mapper.AppointmentMapper;
import com.medislot.coreservice.repository.AppointmentRepository;
import com.medislot.coreservice.repository.AppointmentSlotRepository;
import com.medislot.coreservice.repository.DoctorProfileRepository;
import com.medislot.coreservice.repository.PatientProfileRepository;
import com.medislot.coreservice.security.CurrentUserProvider;
import com.medislot.coreservice.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final AppointmentSlotRepository appointmentSlotRepository;
    private final PatientProfileRepository patientProfileRepository;
    private final DoctorProfileRepository doctorProfileRepository;
    private final AppointmentMapper appointmentMapper;
    private final CurrentUserProvider currentUserProvider;

    @Override
    @Transactional
    public AppointmentResponse bookAppointment(AppointmentBookingRequest request) {
        PatientProfile patientProfile = getCurrentPatientProfile();

        AppointmentSlot slot = appointmentSlotRepository.findById(request.getSlotId())
                .orElseThrow(() -> new SlotNotFoundException("Slot not found with id: " + request.getSlotId()));

        if (slot.getSlotStatus() != SlotStatus.AVAILABLE) {
            throw new SlotAlreadyBookedException("Slot already booked.");
        }

        slot.setSlotStatus(SlotStatus.BOOKED);

        try {
            appointmentSlotRepository.saveAndFlush(slot);
        } catch (ObjectOptimisticLockingFailureException ex) {
            throw new SlotAlreadyBookedException("Slot already booked.");
        }

        Appointment appointment = Appointment.builder()
                .patientProfile(patientProfile)
                .doctorProfile(slot.getDoctorProfile())
                .appointmentSlot(slot)
                .appointmentStatus(AppointmentStatus.PENDING_PAYMENT)
                .bookingTime(LocalDateTime.now())
                .build();

        Appointment saved = appointmentRepository.save(appointment);
        return appointmentMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PatientAppointmentResponse> getPatientAppointments() {
        PatientProfile patientProfile = getCurrentPatientProfile();
        return appointmentRepository.findByPatientProfileIdOrderByBookingTimeDesc(patientProfile.getId())
                .stream()
                .map(appointmentMapper::toPatientResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DoctorAppointmentResponse> getDoctorAppointments() {
        DoctorProfile doctorProfile = getCurrentDoctorProfile();
        return appointmentRepository.findByDoctorProfileIdOrderByBookingTimeDesc(doctorProfile.getId())
                .stream()
                .map(appointmentMapper::toDoctorResponse)
                .toList();
    }

    @Override
    @Transactional
    public AppointmentStatusResponse updateAppointmentStatus(UpdateAppointmentStatusRequest request) {
        DoctorProfile doctorProfile = getCurrentDoctorProfile();

        if (request.getStatus() != AppointmentStatus.COMPLETED
                && request.getStatus() != AppointmentStatus.CANCELLED
                && request.getStatus() != AppointmentStatus.NO_SHOW) {
            throw new InvalidAppointmentStatusException(
                    "Status must be one of: COMPLETED, CANCELLED, NO_SHOW");
        }

        Appointment appointment = appointmentRepository.findById(request.getAppointmentId())
                .orElseThrow(() -> new AppointmentNotFoundException(
                        "Appointment not found with id: " + request.getAppointmentId()));

        if (!appointment.getDoctorProfile().getId().equals(doctorProfile.getId())) {
            throw new AppointmentNotFoundException(
                    "Appointment not found with id: " + request.getAppointmentId());
        }

        if (appointment.getAppointmentStatus() != AppointmentStatus.CONFIRMED) {
            throw new InvalidAppointmentStatusException(
                    "Cannot update appointment. Current status is " + appointment.getAppointmentStatus()
                            + ", only CONFIRMED appointments can be updated.");
        }

        appointment.setAppointmentStatus(request.getStatus());
        Appointment updated = appointmentRepository.save(appointment);

        return appointmentMapper.toStatusResponse(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DoctorAppointmentResponse> filterAppointments(String statusParam, LocalDate date) {
        DoctorProfile doctorProfile = getCurrentDoctorProfile();

        AppointmentStatus status = null;
        if (statusParam != null && !statusParam.isBlank()) {
            try {
                status = AppointmentStatus.valueOf(statusParam.toUpperCase());
            } catch (IllegalArgumentException ex) {
                throw new InvalidAppointmentStatusException("Invalid status: " + statusParam);
            }
        }

        return appointmentRepository.filterAppointments(doctorProfile.getId(), status, date)
                .stream()
                .map(appointmentMapper::toDoctorResponse)
                .toList();
    }

    private PatientProfile getCurrentPatientProfile() {
        User currentUser = currentUserProvider.getCurrentUser();
        return patientProfileRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> new ProfileNotFoundException("Patient profile not found. Please create one first."));
    }

    private DoctorProfile getCurrentDoctorProfile() {
        User currentUser = currentUserProvider.getCurrentUser();
        return doctorProfileRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> new ProfileNotFoundException("Doctor profile not found. Please create one first."));
    }
}
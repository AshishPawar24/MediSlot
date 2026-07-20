package com.medislot.coreservice.controller;

import com.medislot.coreservice.dto.request.CreatePaymentRequest;
import com.medislot.coreservice.dto.request.VerifyPaymentRequest;
import com.medislot.coreservice.dto.response.CreatePaymentResponse;
import com.medislot.coreservice.dto.response.PaymentResponse;
import com.medislot.coreservice.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/patient/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/create-order")
    public ResponseEntity<CreatePaymentResponse> createOrder(@Valid @RequestBody CreatePaymentRequest request) {
        return ResponseEntity.ok(paymentService.createOrder(request));
    }

    @PostMapping("/verify")
    public ResponseEntity<PaymentResponse> verifyPayment(@Valid @RequestBody VerifyPaymentRequest request) {
        return ResponseEntity.ok(paymentService.verifyPayment(request));
    }
}
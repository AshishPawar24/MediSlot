// service/PaymentService.java
package com.medislot.coreservice.service;

import com.medislot.coreservice.dto.request.CreatePaymentRequest;
import com.medislot.coreservice.dto.request.VerifyPaymentRequest;
import com.medislot.coreservice.dto.response.CreatePaymentResponse;
import com.medislot.coreservice.dto.response.PaymentResponse;

public interface PaymentService {
    CreatePaymentResponse createOrder(CreatePaymentRequest request);
    PaymentResponse verifyPayment(VerifyPaymentRequest request);
    void processWebhookEvent(String payload, String signatureHeader);
}
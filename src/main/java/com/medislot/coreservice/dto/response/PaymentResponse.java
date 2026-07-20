// dto/response/PaymentResponse.java
package com.medislot.coreservice.dto.response;

import com.medislot.coreservice.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class PaymentResponse {
    private Long id;
    private Long appointmentId;
    private String razorpayOrderId;
    private String razorpayPaymentId;
    private PaymentStatus paymentStatus;
    private Double amount;
    private String currency;
}
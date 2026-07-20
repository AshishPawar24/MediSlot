// dto/response/CreatePaymentResponse.java
package com.medislot.coreservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CreatePaymentResponse {
    private String razorpayOrderId;
    private String razorpayKeyId;
    private Double amount;
    private String currency;
    private Long appointmentId;
}
// dto/request/CreatePaymentRequest.java
package com.medislot.coreservice.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatePaymentRequest {
    @NotNull(message = "Appointment id is required")
    private Long appointmentId;
}
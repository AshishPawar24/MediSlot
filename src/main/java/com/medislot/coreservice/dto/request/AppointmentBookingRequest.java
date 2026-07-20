// dto/request/AppointmentBookingRequest.java
package com.medislot.coreservice.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppointmentBookingRequest {
    @NotNull(message = "Slot id is required")
    private Long slotId;
}
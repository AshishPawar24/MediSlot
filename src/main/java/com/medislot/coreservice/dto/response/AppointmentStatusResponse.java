package com.medislot.coreservice.dto.response;

import com.medislot.coreservice.enums.AppointmentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class AppointmentStatusResponse {
    private Long appointmentId;
    private AppointmentStatus status;
    private LocalDateTime updatedAt;
}
// dto/response/DoctorAppointmentResponse.java
package com.medislot.coreservice.dto.response;

import com.medislot.coreservice.enums.AppointmentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Builder
@AllArgsConstructor
public class DoctorAppointmentResponse {
    private Long id;
    private String patientName;
    private String patientPhone;
    private LocalDate appointmentDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private AppointmentStatus appointmentStatus;
}
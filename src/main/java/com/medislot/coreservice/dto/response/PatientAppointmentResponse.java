// dto/response/PatientAppointmentResponse.java
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
public class PatientAppointmentResponse {
    private Long id;
    private String doctorName;
    private String specialization;
    private String hospital;
    private String city;
    private LocalDate appointmentDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private AppointmentStatus appointmentStatus;
}
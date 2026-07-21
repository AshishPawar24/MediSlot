package com.medislot.coreservice.controller;

import com.medislot.coreservice.dto.request.AppointmentBookingRequest;
import com.medislot.coreservice.dto.request.UpdateAppointmentStatusRequest;
import com.medislot.coreservice.dto.response.AppointmentResponse;
import com.medislot.coreservice.dto.response.AppointmentStatusResponse;
import com.medislot.coreservice.dto.response.DoctorAppointmentResponse;
import com.medislot.coreservice.dto.response.PatientAppointmentResponse;
import com.medislot.coreservice.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping("/api/patient/appointments")
    public ResponseEntity<AppointmentResponse> bookAppointment(@Valid @RequestBody AppointmentBookingRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(appointmentService.bookAppointment(request));
    }

    @GetMapping("/api/patient/appointments")
    public ResponseEntity<List<PatientAppointmentResponse>> getPatientAppointments() {
        return ResponseEntity.ok(appointmentService.getPatientAppointments());
    }

    @GetMapping("/api/doctor/appointments")
    public ResponseEntity<List<DoctorAppointmentResponse>> getDoctorAppointments() {
        return ResponseEntity.ok(appointmentService.getDoctorAppointments());
    }

    @PutMapping("/api/doctor/appointments/status")
    public ResponseEntity<AppointmentStatusResponse> updateAppointmentStatus(
            @Valid @RequestBody UpdateAppointmentStatusRequest request) {
        return ResponseEntity.ok(appointmentService.updateAppointmentStatus(request));
    }

    @GetMapping("/api/doctor/appointments/history")
    public ResponseEntity<List<DoctorAppointmentResponse>> getAppointmentHistory() {
        return ResponseEntity.ok(appointmentService.getDoctorAppointments());
    }

    @GetMapping("/api/doctor/appointments/filter")
    public ResponseEntity<List<DoctorAppointmentResponse>> filterAppointments(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(appointmentService.filterAppointments(status, date));
    }
}
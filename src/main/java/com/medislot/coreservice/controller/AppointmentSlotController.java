package com.medislot.coreservice.controller;

import com.medislot.coreservice.dto.request.AppointmentSlotRequest;
import com.medislot.coreservice.dto.response.AppointmentSlotResponse;
import com.medislot.coreservice.dto.response.DoctorAvailableSlotResponse;
import com.medislot.coreservice.service.AppointmentSlotService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AppointmentSlotController {

    private final AppointmentSlotService appointmentSlotService;

    @PostMapping("/api/doctor/slots")
    public ResponseEntity<AppointmentSlotResponse> createSlot(@Valid @RequestBody AppointmentSlotRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(appointmentSlotService.createSlot(request));
    }

    @GetMapping("/api/doctor/slots")
    public ResponseEntity<List<AppointmentSlotResponse>> getMySlots() {
        return ResponseEntity.ok(appointmentSlotService.getMySlots());
    }

    @DeleteMapping("/api/doctor/slots/{slotId}")
    public ResponseEntity<Void> deleteSlot(@PathVariable Long slotId) {
        appointmentSlotService.deleteSlot(slotId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/api/patient/doctors/{doctorId}/slots")
    public ResponseEntity<List<DoctorAvailableSlotResponse>> getDoctorAvailableSlots(@PathVariable Long doctorId) {
        return ResponseEntity.ok(appointmentSlotService.getDoctorAvailableSlots(doctorId));
    }
}
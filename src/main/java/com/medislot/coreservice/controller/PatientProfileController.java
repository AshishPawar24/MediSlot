// controller/PatientProfileController.java
package com.medislot.coreservice.controller;

import com.medislot.coreservice.dto.request.PatientProfileRequest;
import com.medislot.coreservice.dto.response.PatientProfileResponse;
import com.medislot.coreservice.service.PatientProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/patient/profile")
@RequiredArgsConstructor
public class PatientProfileController {

    private final PatientProfileService patientProfileService;

    @PostMapping
    public ResponseEntity<PatientProfileResponse> createProfile(@Valid @RequestBody PatientProfileRequest request) {
        PatientProfileResponse response = patientProfileService.createProfile(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<PatientProfileResponse> getMyProfile() {
        return ResponseEntity.ok(patientProfileService.getMyProfile());
    }

    @PutMapping
    public ResponseEntity<PatientProfileResponse> updateProfile(@Valid @RequestBody PatientProfileRequest request) {
        return ResponseEntity.ok(patientProfileService.updateProfile(request));
    }
}
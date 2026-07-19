// controller/DoctorProfileController.java
package com.medislot.coreservice.controller;

import com.medislot.coreservice.dto.request.DoctorProfileRequest;
import com.medislot.coreservice.dto.response.DoctorProfileResponse;
import com.medislot.coreservice.service.DoctorProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/doctor/profile")
@RequiredArgsConstructor
public class DoctorProfileController {

    private final DoctorProfileService doctorProfileService;

    @PostMapping
    public ResponseEntity<DoctorProfileResponse> createProfile(@Valid @RequestBody DoctorProfileRequest request) {
        DoctorProfileResponse response = doctorProfileService.createProfile(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<DoctorProfileResponse> getMyProfile() {
        return ResponseEntity.ok(doctorProfileService.getMyProfile());
    }

    @PutMapping
    public ResponseEntity<DoctorProfileResponse> updateProfile(@Valid @RequestBody DoctorProfileRequest request) {
        return ResponseEntity.ok(doctorProfileService.updateProfile(request));
    }
}
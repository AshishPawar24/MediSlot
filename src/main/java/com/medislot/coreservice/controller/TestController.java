package com.medislot.coreservice.controller;

import com.medislot.coreservice.entity.DoctorProfile;
import com.medislot.coreservice.entity.PatientProfile;
import com.medislot.coreservice.entity.User;
import com.medislot.coreservice.repository.DoctorProfileRepository;
import com.medislot.coreservice.repository.PatientProfileRepository;
import com.medislot.coreservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestController {

    private final UserRepository userRepository;
    private final PatientProfileRepository patientProfileRepository;
    private final DoctorProfileRepository doctorProfileRepository;

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User saved = userRepository.save(user);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    @PostMapping("/patient-profiles/{userId}")
    public ResponseEntity<PatientProfile> createPatientProfile(
            @PathVariable Long userId,
            @RequestBody PatientProfile profile) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        profile.setUser(user);
        return ResponseEntity.ok(patientProfileRepository.save(profile));
    }

    @PostMapping("/doctor-profiles/{userId}")
    public ResponseEntity<DoctorProfile> createDoctorProfile(
            @PathVariable Long userId,
            @RequestBody DoctorProfile profile) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        profile.setUser(user);
        return ResponseEntity.ok(doctorProfileRepository.save(profile));
    }

    @GetMapping("/patient-profiles")
    public ResponseEntity<List<PatientProfile>> getAllPatientProfiles() {
        return ResponseEntity.ok(patientProfileRepository.findAll());
    }

    @GetMapping("/doctor-profiles")
    public ResponseEntity<List<DoctorProfile>> getAllDoctorProfiles() {
        return ResponseEntity.ok(doctorProfileRepository.findAll());
    }
}
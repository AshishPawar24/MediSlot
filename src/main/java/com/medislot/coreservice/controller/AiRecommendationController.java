package com.medislot.coreservice.controller;

import com.medislot.coreservice.dto.request.SymptomRequest;
import com.medislot.coreservice.dto.response.RecommendedDoctorResponse;
import com.medislot.coreservice.service.AiRecommendationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/patient/ai")
@RequiredArgsConstructor
public class AiRecommendationController {

    private final AiRecommendationService aiRecommendationService;

    @PostMapping("/recommendation")
    public ResponseEntity<RecommendedDoctorResponse> getRecommendation(@Valid @RequestBody SymptomRequest request) {
        return ResponseEntity.ok(aiRecommendationService.getRecommendation(request));
    }
}
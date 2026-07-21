package com.medislot.coreservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class RecommendedDoctorResponse {
    private List<String> recommendedSpecializations;
    private List<DoctorSearchResponse> matchingDoctors;
    private Integer confidence;
    private String medicalDisclaimer;
}
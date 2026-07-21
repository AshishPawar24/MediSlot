package com.medislot.coreservice.service;

import com.medislot.coreservice.dto.request.SymptomRequest;
import com.medislot.coreservice.dto.response.RecommendedDoctorResponse;

public interface AiRecommendationService {
    RecommendedDoctorResponse getRecommendation(SymptomRequest request);
}
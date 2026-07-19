// service/PatientProfileService.java
package com.medislot.coreservice.service;

import com.medislot.coreservice.dto.request.PatientProfileRequest;
import com.medislot.coreservice.dto.response.PatientProfileResponse;

public interface PatientProfileService {
    PatientProfileResponse createProfile(PatientProfileRequest request);
    PatientProfileResponse getMyProfile();
    PatientProfileResponse updateProfile(PatientProfileRequest request);
}
// service/DoctorProfileService.java
package com.medislot.coreservice.service;

import com.medislot.coreservice.dto.request.DoctorProfileRequest;
import com.medislot.coreservice.dto.response.DoctorProfileResponse;

public interface DoctorProfileService {
    DoctorProfileResponse createProfile(DoctorProfileRequest request);
    DoctorProfileResponse getMyProfile();
    DoctorProfileResponse updateProfile(DoctorProfileRequest request);
}
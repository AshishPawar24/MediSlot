// service/impl/PatientProfileServiceImpl.java
package com.medislot.coreservice.service.impl;

import com.medislot.coreservice.dto.request.PatientProfileRequest;
import com.medislot.coreservice.dto.response.PatientProfileResponse;
import com.medislot.coreservice.entity.PatientProfile;
import com.medislot.coreservice.entity.User;
import com.medislot.coreservice.exception.ProfileAlreadyExistsException;
import com.medislot.coreservice.exception.ProfileNotFoundException;
import com.medislot.coreservice.mapper.PatientProfileMapper;
import com.medislot.coreservice.repository.PatientProfileRepository;
import com.medislot.coreservice.security.CurrentUserProvider;
import com.medislot.coreservice.service.PatientProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PatientProfileServiceImpl implements PatientProfileService {

    private final PatientProfileRepository patientProfileRepository;
    private final PatientProfileMapper patientProfileMapper;
    private final CurrentUserProvider currentUserProvider;

    @Override
    @Transactional
    public PatientProfileResponse createProfile(PatientProfileRequest request) {
        User currentUser = currentUserProvider.getCurrentUser();

        if (patientProfileRepository.findByUserId(currentUser.getId()).isPresent()) {
            throw new ProfileAlreadyExistsException("Patient profile already exists for this account");
        }

        PatientProfile profile = patientProfileMapper.toEntity(request);
        profile.setUser(currentUser);
        PatientProfile saved = patientProfileRepository.save(profile);

        return patientProfileMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public PatientProfileResponse getMyProfile() {
        User currentUser = currentUserProvider.getCurrentUser();

        PatientProfile profile = patientProfileRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> new ProfileNotFoundException("Patient profile not found. Please create one first."));

        return patientProfileMapper.toResponse(profile);
    }

    @Override
    @Transactional
    public PatientProfileResponse updateProfile(PatientProfileRequest request) {
        User currentUser = currentUserProvider.getCurrentUser();

        PatientProfile profile = patientProfileRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> new ProfileNotFoundException("Patient profile not found. Please create one first."));

        patientProfileMapper.updateEntityFromRequest(request, profile);
        PatientProfile updated = patientProfileRepository.save(profile);

        return patientProfileMapper.toResponse(updated);
    }
}
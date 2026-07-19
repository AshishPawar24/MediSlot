// service/impl/DoctorProfileServiceImpl.java
package com.medislot.coreservice.service.impl;

import com.medislot.coreservice.dto.request.DoctorProfileRequest;
import com.medislot.coreservice.dto.response.DoctorProfileResponse;
import com.medislot.coreservice.entity.DoctorProfile;
import com.medislot.coreservice.entity.User;
import com.medislot.coreservice.exception.ProfileAlreadyExistsException;
import com.medislot.coreservice.exception.ProfileNotFoundException;
import com.medislot.coreservice.mapper.DoctorProfileMapper;
import com.medislot.coreservice.repository.DoctorProfileRepository;
import com.medislot.coreservice.security.CurrentUserProvider;
import com.medislot.coreservice.service.DoctorProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DoctorProfileServiceImpl implements DoctorProfileService {

    private final DoctorProfileRepository doctorProfileRepository;
    private final DoctorProfileMapper doctorProfileMapper;
    private final CurrentUserProvider currentUserProvider;

    @Override
    @Transactional
    public DoctorProfileResponse createProfile(DoctorProfileRequest request) {
        User currentUser = currentUserProvider.getCurrentUser();

        if (doctorProfileRepository.findByUserId(currentUser.getId()).isPresent()) {
            throw new ProfileAlreadyExistsException("Doctor profile already exists for this account");
        }

        DoctorProfile profile = doctorProfileMapper.toEntity(request);
        profile.setUser(currentUser);
        DoctorProfile saved = doctorProfileRepository.save(profile);

        return doctorProfileMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public DoctorProfileResponse getMyProfile() {
        User currentUser = currentUserProvider.getCurrentUser();

        DoctorProfile profile = doctorProfileRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> new ProfileNotFoundException("Doctor profile not found. Please create one first."));

        return doctorProfileMapper.toResponse(profile);
    }

    @Override
    @Transactional
    public DoctorProfileResponse updateProfile(DoctorProfileRequest request) {
        User currentUser = currentUserProvider.getCurrentUser();

        DoctorProfile profile = doctorProfileRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> new ProfileNotFoundException("Doctor profile not found. Please create one first."));

        doctorProfileMapper.updateEntityFromRequest(request, profile);
        DoctorProfile updated = doctorProfileRepository.save(profile);

        return doctorProfileMapper.toResponse(updated);
    }
}
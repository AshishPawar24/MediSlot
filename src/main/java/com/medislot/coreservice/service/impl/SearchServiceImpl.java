// service/impl/SearchServiceImpl.java
package com.medislot.coreservice.service.impl;

import com.medislot.coreservice.dto.request.SearchDoctorRequest;
import com.medislot.coreservice.dto.response.DoctorSearchResponse;
import com.medislot.coreservice.entity.DoctorProfile;
import com.medislot.coreservice.mapper.DoctorProfileMapper;
import com.medislot.coreservice.repository.DoctorProfileRepository;
import com.medislot.coreservice.service.SearchService;
import com.medislot.coreservice.specification.DoctorSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

    private final DoctorProfileRepository doctorProfileRepository;
    private final DoctorProfileMapper doctorProfileMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<DoctorSearchResponse> searchDoctors(SearchDoctorRequest request) {

        Specification<DoctorProfile> spec = Specification
                .where(DoctorSpecification.hasCity(request.getCity()))
                .and(DoctorSpecification.hasSpecialization(request.getSpecialization()))
                .and(DoctorSpecification.hasMinimumExperience(request.getMinimumExperience()))
                .and(DoctorSpecification.hasMaximumConsultationFee(request.getMaximumConsultationFee()));

        Sort sort = Sort.by(
                request.getSortDirection().equalsIgnoreCase("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC,
                request.getSortBy()
        );

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);

        return doctorProfileRepository.findAll(spec, pageable)
                .map(doctorProfileMapper::toSearchResponse);
    }
}
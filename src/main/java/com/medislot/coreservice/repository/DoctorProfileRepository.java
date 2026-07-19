package com.medislot.coreservice.repository;

import com.medislot.coreservice.entity.DoctorProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface DoctorProfileRepository
        extends JpaRepository<DoctorProfile, Long>, JpaSpecificationExecutor<DoctorProfile> {

    Optional<DoctorProfile> findByUserId(Long userId);
}
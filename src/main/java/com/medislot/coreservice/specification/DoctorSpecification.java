package com.medislot.coreservice.specification;

import com.medislot.coreservice.entity.DoctorProfile;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class DoctorSpecification {

    private DoctorSpecification() {
    }

    public static Specification<DoctorProfile> hasCity(String city) {
        return (root, query, cb) ->
                StringUtils.hasText(city) ? cb.equal(cb.lower(root.get("city")), city.toLowerCase()) : null;
    }

    public static Specification<DoctorProfile> hasSpecialization(String specialization) {
        return (root, query, cb) ->
                StringUtils.hasText(specialization)
                        ? cb.equal(cb.lower(root.get("specialization")), specialization.toLowerCase())
                        : null;
    }

    public static Specification<DoctorProfile> hasMinimumExperience(Integer minimumExperience) {
        return (root, query, cb) ->
                minimumExperience != null ? cb.greaterThanOrEqualTo(root.get("experience"), minimumExperience) : null;
    }

    public static Specification<DoctorProfile> hasMaximumConsultationFee(Double maximumFee) {
        return (root, query, cb) ->
                maximumFee != null ? cb.lessThanOrEqualTo(root.get("consultationFee"), maximumFee) : null;
    }
}
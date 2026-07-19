package com.medislot.coreservice.dto.request;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchDoctorRequest {

    private String city;
    private String specialization;

    @Min(value = 0, message = "Minimum experience cannot be negative")
    private Integer minimumExperience;

    @Min(value = 0, message = "Maximum consultation fee cannot be negative")
    private Double maximumConsultationFee;

    @Min(value = 0, message = "Page number cannot be negative")
    private int page = 0;

    @Min(value = 1, message = "Page size must be at least 1")
    private int size = 10;

    private String sortBy = "experience";
    private String sortDirection = "DESC";
}
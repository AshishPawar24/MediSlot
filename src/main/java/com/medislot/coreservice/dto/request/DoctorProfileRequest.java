// dto/request/DoctorProfileRequest.java
package com.medislot.coreservice.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DoctorProfileRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Qualification is required")
    private String qualification;

    @NotNull(message = "Experience is required")
    @Min(value = 0, message = "Experience cannot be negative")
    @Max(value = 60, message = "Experience must be realistic")
    private Integer experience;

    @NotBlank(message = "Hospital is required")
    private String hospital;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "Specialization is required")
    private String specialization;

    @NotNull(message = "Consultation fee is required")
    @Positive(message = "Consultation fee must be greater than zero")
    private Double consultationFee;

    @Size(max = 1000, message = "Biography cannot exceed 1000 characters")
    private String biography;
}
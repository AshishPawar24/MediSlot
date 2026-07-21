package com.medislot.coreservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SymptomRequest {

    @NotBlank(message = "Symptoms are required")
    @Size(min = 10, max = 1000, message = "Symptoms must be between 10 and 1000 characters")
    private String symptoms;
}
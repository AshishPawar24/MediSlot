// dto/response/DoctorProfileResponse.java
package com.medislot.coreservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class DoctorProfileResponse {
    private Long id;
    private String name;
    private String qualification;
    private Integer experience;
    private String hospital;
    private String city;
    private String specialization;
    private Double consultationFee;
    private String biography;
    private String email;
}
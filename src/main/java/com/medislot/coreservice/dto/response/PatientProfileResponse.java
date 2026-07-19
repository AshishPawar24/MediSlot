// dto/response/PatientProfileResponse.java
package com.medislot.coreservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class PatientProfileResponse {
    private Long id;
    private String name;
    private Integer age;
    private String gender;
    private String phone;
    private String city;
    private String email;
}
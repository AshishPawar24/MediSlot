// mapper/PatientProfileMapper.java
package com.medislot.coreservice.mapper;

import com.medislot.coreservice.dto.request.PatientProfileRequest;
import com.medislot.coreservice.dto.response.PatientProfileResponse;
import com.medislot.coreservice.entity.PatientProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PatientProfileMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    PatientProfile toEntity(PatientProfileRequest request);

    @Mapping(target = "email", source = "user.email")
    PatientProfileResponse toResponse(PatientProfile profile);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    void updateEntityFromRequest(PatientProfileRequest request, @MappingTarget PatientProfile profile);
}
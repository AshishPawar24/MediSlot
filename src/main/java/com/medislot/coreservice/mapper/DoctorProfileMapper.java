// mapper/DoctorProfileMapper.java
package com.medislot.coreservice.mapper;

import com.medislot.coreservice.dto.request.DoctorProfileRequest;
import com.medislot.coreservice.dto.response.DoctorProfileResponse;
import com.medislot.coreservice.dto.response.DoctorSearchResponse;
import com.medislot.coreservice.entity.DoctorProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface DoctorProfileMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    DoctorProfile toEntity(DoctorProfileRequest request);

    @Mapping(target = "email", source = "user.email")
    DoctorProfileResponse toResponse(DoctorProfile profile);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    void updateEntityFromRequest(DoctorProfileRequest request, @MappingTarget DoctorProfile profile);

    DoctorSearchResponse toSearchResponse(DoctorProfile profile);
}
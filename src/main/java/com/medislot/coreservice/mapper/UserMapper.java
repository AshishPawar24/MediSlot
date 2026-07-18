package com.medislot.coreservice.mapper;

import com.medislot.coreservice.dto.request.RegisterRequest;
import com.medislot.coreservice.dto.response.UserResponse;
import com.medislot.coreservice.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    User toEntity(RegisterRequest request);

    UserResponse toResponse(User user);
}
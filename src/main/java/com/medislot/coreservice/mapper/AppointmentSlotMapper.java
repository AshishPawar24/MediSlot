package com.medislot.coreservice.mapper;

import com.medislot.coreservice.dto.request.AppointmentSlotRequest;
import com.medislot.coreservice.dto.response.AppointmentSlotResponse;
import com.medislot.coreservice.dto.response.DoctorAvailableSlotResponse;
import com.medislot.coreservice.entity.AppointmentSlot;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AppointmentSlotMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "doctorProfile", ignore = true)
    @Mapping(target = "slotStatus", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    AppointmentSlot toEntity(AppointmentSlotRequest request);

    AppointmentSlotResponse toResponse(AppointmentSlot slot);

    DoctorAvailableSlotResponse toAvailableSlotResponse(AppointmentSlot slot);
}
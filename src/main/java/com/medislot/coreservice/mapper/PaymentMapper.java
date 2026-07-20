package com.medislot.coreservice.mapper;

import com.medislot.coreservice.dto.response.PaymentResponse;
import com.medislot.coreservice.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    @Mapping(target = "appointmentId", source = "appointment.id")
    PaymentResponse toResponse(Payment payment);
}
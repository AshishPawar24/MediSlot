package com.medislot.coreservice.mapper;

import com.medislot.coreservice.dto.response.AppointmentResponse;
import com.medislot.coreservice.dto.response.AppointmentStatusResponse;
import com.medislot.coreservice.dto.response.DoctorAppointmentResponse;
import com.medislot.coreservice.dto.response.PatientAppointmentResponse;
import com.medislot.coreservice.entity.Appointment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {

    @Mapping(target = "doctorName", source = "doctorProfile.name")
    @Mapping(target = "specialization", source = "doctorProfile.specialization")
    @Mapping(target = "appointmentDate", source = "appointmentSlot.appointmentDate")
    @Mapping(target = "startTime", source = "appointmentSlot.startTime")
    @Mapping(target = "endTime", source = "appointmentSlot.endTime")
    AppointmentResponse toResponse(Appointment appointment);

    @Mapping(target = "doctorName", source = "doctorProfile.name")
    @Mapping(target = "specialization", source = "doctorProfile.specialization")
    @Mapping(target = "hospital", source = "doctorProfile.hospital")
    @Mapping(target = "city", source = "doctorProfile.city")
    @Mapping(target = "appointmentDate", source = "appointmentSlot.appointmentDate")
    @Mapping(target = "startTime", source = "appointmentSlot.startTime")
    @Mapping(target = "endTime", source = "appointmentSlot.endTime")
    PatientAppointmentResponse toPatientResponse(Appointment appointment);

    @Mapping(target = "patientName", source = "patientProfile.name")
    @Mapping(target = "patientPhone", source = "patientProfile.phone")
    @Mapping(target = "appointmentDate", source = "appointmentSlot.appointmentDate")
    @Mapping(target = "startTime", source = "appointmentSlot.startTime")
    @Mapping(target = "endTime", source = "appointmentSlot.endTime")
    DoctorAppointmentResponse toDoctorResponse(Appointment appointment);

    @Mapping(target = "appointmentId", source = "id")
    @Mapping(target = "status", source = "appointmentStatus")
    AppointmentStatusResponse toStatusResponse(Appointment appointment);
}
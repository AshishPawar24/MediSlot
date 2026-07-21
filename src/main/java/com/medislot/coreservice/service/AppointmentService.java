package com.medislot.coreservice.service;

import com.medislot.coreservice.dto.request.AppointmentBookingRequest;
import com.medislot.coreservice.dto.request.UpdateAppointmentStatusRequest;
import com.medislot.coreservice.dto.response.AppointmentResponse;
import com.medislot.coreservice.dto.response.AppointmentStatusResponse;
import com.medislot.coreservice.dto.response.DoctorAppointmentResponse;
import com.medislot.coreservice.dto.response.PatientAppointmentResponse;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentService {
    AppointmentResponse bookAppointment(AppointmentBookingRequest request);
    List<PatientAppointmentResponse> getPatientAppointments();
    List<DoctorAppointmentResponse> getDoctorAppointments();
    AppointmentStatusResponse updateAppointmentStatus(UpdateAppointmentStatusRequest request);
    List<DoctorAppointmentResponse> filterAppointments(String status, LocalDate date);
}
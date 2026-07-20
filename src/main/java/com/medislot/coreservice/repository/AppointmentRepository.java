package com.medislot.coreservice.repository;

import com.medislot.coreservice.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByPatientProfileIdOrderByBookingTimeDesc(Long patientProfileId);
    List<Appointment> findByDoctorProfileIdOrderByBookingTimeDesc(Long doctorProfileId);
}
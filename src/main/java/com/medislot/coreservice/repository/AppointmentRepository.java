package com.medislot.coreservice.repository;

import com.medislot.coreservice.entity.Appointment;
import com.medislot.coreservice.enums.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @Query("SELECT a FROM Appointment a " +
            "JOIN FETCH a.doctorProfile " +
            "JOIN FETCH a.appointmentSlot " +
            "WHERE a.patientProfile.id = :patientProfileId " +
            "ORDER BY a.bookingTime DESC")
    List<Appointment> findByPatientProfileIdWithDetails(@Param("patientProfileId") Long patientProfileId);

    @Query("SELECT a FROM Appointment a " +
            "JOIN FETCH a.patientProfile " +
            "JOIN FETCH a.appointmentSlot " +
            "WHERE a.doctorProfile.id = :doctorProfileId " +
            "ORDER BY a.bookingTime DESC")
    List<Appointment> findByDoctorProfileIdWithDetails(@Param("doctorProfileId") Long doctorProfileId);

    @Query("SELECT a FROM Appointment a " +
            "JOIN FETCH a.patientProfile " +
            "JOIN FETCH a.appointmentSlot " +
            "WHERE a.doctorProfile.id = :doctorProfileId " +
            "AND (:status IS NULL OR a.appointmentStatus = :status) " +
            "AND (:date IS NULL OR a.appointmentSlot.appointmentDate = :date) " +
            "ORDER BY a.bookingTime DESC")
    List<Appointment> filterAppointments(@Param("doctorProfileId") Long doctorProfileId,
                                         @Param("status") AppointmentStatus status,
                                         @Param("date") LocalDate date);
}
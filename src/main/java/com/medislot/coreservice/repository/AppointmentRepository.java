package com.medislot.coreservice.repository;

import com.medislot.coreservice.entity.Appointment;
import com.medislot.coreservice.enums.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByPatientProfileIdOrderByBookingTimeDesc(Long patientProfileId);
    List<Appointment> findByDoctorProfileIdOrderByBookingTimeDesc(Long doctorProfileId);

    @Query("SELECT a FROM Appointment a " +
            "WHERE a.doctorProfile.id = :doctorProfileId " +
            "AND (:status IS NULL OR a.appointmentStatus = :status) " +
            "AND (:date IS NULL OR a.appointmentSlot.appointmentDate = :date) " +
            "ORDER BY a.bookingTime DESC")
    List<Appointment> filterAppointments(@Param("doctorProfileId") Long doctorProfileId,
                                         @Param("status") AppointmentStatus status,
                                         @Param("date") LocalDate date);
}
package com.medislot.coreservice.repository;

import com.medislot.coreservice.entity.AppointmentSlot;
import com.medislot.coreservice.enums.SlotStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface AppointmentSlotRepository extends JpaRepository<AppointmentSlot, Long> {

    List<AppointmentSlot> findByDoctorProfileIdOrderByAppointmentDateAscStartTimeAsc(Long doctorProfileId);

    List<AppointmentSlot> findByDoctorProfileIdAndSlotStatusOrderByAppointmentDateAscStartTimeAsc(
            Long doctorProfileId, SlotStatus slotStatus);

    @Query("SELECT COUNT(s) > 0 FROM AppointmentSlot s " +
            "WHERE s.doctorProfile.id = :doctorProfileId " +
            "AND s.appointmentDate = :appointmentDate " +
            "AND s.slotStatus <> com.medislot.coreservice.enums.SlotStatus.CANCELLED " +
            "AND s.startTime < :endTime AND s.endTime > :startTime")
    boolean existsOverlappingSlot(@Param("doctorProfileId") Long doctorProfileId,
                                  @Param("appointmentDate") LocalDate appointmentDate,
                                  @Param("startTime") LocalTime startTime,
                                  @Param("endTime") LocalTime endTime);
}
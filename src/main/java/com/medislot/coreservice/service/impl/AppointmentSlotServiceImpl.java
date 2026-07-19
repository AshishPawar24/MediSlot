// service/impl/AppointmentSlotServiceImpl.java
package com.medislot.coreservice.service.impl;

import com.medislot.coreservice.dto.request.AppointmentSlotRequest;
import com.medislot.coreservice.dto.response.AppointmentSlotResponse;
import com.medislot.coreservice.dto.response.DoctorAvailableSlotResponse;
import com.medislot.coreservice.entity.AppointmentSlot;
import com.medislot.coreservice.entity.DoctorProfile;
import com.medislot.coreservice.entity.User;
import com.medislot.coreservice.enums.SlotStatus;
import com.medislot.coreservice.exception.InvalidSlotException;
import com.medislot.coreservice.exception.ProfileNotFoundException;
import com.medislot.coreservice.exception.SlotNotFoundException;
import com.medislot.coreservice.exception.SlotOverlapException;
import com.medislot.coreservice.mapper.AppointmentSlotMapper;
import com.medislot.coreservice.repository.AppointmentSlotRepository;
import com.medislot.coreservice.repository.DoctorProfileRepository;
import com.medislot.coreservice.security.CurrentUserProvider;
import com.medislot.coreservice.service.AppointmentSlotService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentSlotServiceImpl implements AppointmentSlotService {

    private final AppointmentSlotRepository appointmentSlotRepository;
    private final DoctorProfileRepository doctorProfileRepository;
    private final AppointmentSlotMapper appointmentSlotMapper;
    private final CurrentUserProvider currentUserProvider;

    @Override
    @Transactional
    public AppointmentSlotResponse createSlot(AppointmentSlotRequest request) {
        DoctorProfile doctorProfile = getCurrentDoctorProfile();

        if (!request.getEndTime().isAfter(request.getStartTime())) {
            throw new InvalidSlotException("End time must be after start time");
        }

        boolean overlaps = appointmentSlotRepository.existsOverlappingSlot(
                doctorProfile.getId(), request.getAppointmentDate(),
                request.getStartTime(), request.getEndTime());

        if (overlaps) {
            throw new SlotOverlapException("This slot overlaps with an existing slot");
        }

        AppointmentSlot slot = appointmentSlotMapper.toEntity(request);
        slot.setDoctorProfile(doctorProfile);
        slot.setSlotStatus(SlotStatus.AVAILABLE);

        return appointmentSlotMapper.toResponse(appointmentSlotRepository.save(slot));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentSlotResponse> getMySlots() {
        DoctorProfile doctorProfile = getCurrentDoctorProfile();
        return appointmentSlotRepository
                .findByDoctorProfileIdOrderByAppointmentDateAscStartTimeAsc(doctorProfile.getId())
                .stream()
                .map(appointmentSlotMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public void deleteSlot(Long slotId) {
        DoctorProfile doctorProfile = getCurrentDoctorProfile();

        AppointmentSlot slot = appointmentSlotRepository.findById(slotId)
                .orElseThrow(() -> new SlotNotFoundException("Slot not found with id: " + slotId));

        if (!slot.getDoctorProfile().getId().equals(doctorProfile.getId())) {
            // Same message as "not found" — avoids confirming another doctor's slot ID exists
            throw new SlotNotFoundException("Slot not found with id: " + slotId);
        }

        if (slot.getSlotStatus() == SlotStatus.BOOKED) {
            throw new InvalidSlotException("Cannot delete a booked slot");
        }

        appointmentSlotRepository.delete(slot);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DoctorAvailableSlotResponse> getDoctorAvailableSlots(Long doctorId) {
        if (!doctorProfileRepository.existsById(doctorId)) {
            throw new ProfileNotFoundException("Doctor not found with id: " + doctorId);
        }

        return appointmentSlotRepository
                .findByDoctorProfileIdAndSlotStatusOrderByAppointmentDateAscStartTimeAsc(doctorId, SlotStatus.AVAILABLE)
                .stream()
                .map(appointmentSlotMapper::toAvailableSlotResponse)
                .toList();
    }

    private DoctorProfile getCurrentDoctorProfile() {
        User currentUser = currentUserProvider.getCurrentUser();
        return doctorProfileRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> new ProfileNotFoundException("Doctor profile not found. Please create one first."));
    }
}
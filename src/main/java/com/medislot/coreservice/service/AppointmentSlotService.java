// service/AppointmentSlotService.java
package com.medislot.coreservice.service;

import com.medislot.coreservice.dto.request.AppointmentSlotRequest;
import com.medislot.coreservice.dto.response.AppointmentSlotResponse;
import com.medislot.coreservice.dto.response.DoctorAvailableSlotResponse;

import java.util.List;

public interface AppointmentSlotService {
    AppointmentSlotResponse createSlot(AppointmentSlotRequest request);
    List<AppointmentSlotResponse> getMySlots();
    void deleteSlot(Long slotId);
    List<DoctorAvailableSlotResponse> getDoctorAvailableSlots(Long doctorId);
}
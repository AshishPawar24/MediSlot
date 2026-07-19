// service/SearchService.java
package com.medislot.coreservice.service;

import com.medislot.coreservice.dto.request.SearchDoctorRequest;
import com.medislot.coreservice.dto.response.DoctorSearchResponse;
import org.springframework.data.domain.Page;

public interface SearchService {
    Page<DoctorSearchResponse> searchDoctors(SearchDoctorRequest request);
}
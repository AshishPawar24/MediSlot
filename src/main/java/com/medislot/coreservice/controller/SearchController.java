package com.medislot.coreservice.controller;

import com.medislot.coreservice.dto.request.SearchDoctorRequest;
import com.medislot.coreservice.dto.response.DoctorSearchResponse;
import com.medislot.coreservice.service.SearchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @PostMapping("/doctors")
    public ResponseEntity<Page<DoctorSearchResponse>> searchDoctors(@Valid @RequestBody SearchDoctorRequest request) {
        return ResponseEntity.ok(searchService.searchDoctors(request));
    }
}
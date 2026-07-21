package com.medislot.coreservice.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.medislot.coreservice.ai.PromptBuilder;
import com.medislot.coreservice.dto.request.SearchDoctorRequest;
import com.medislot.coreservice.dto.request.SymptomRequest;
import com.medislot.coreservice.dto.response.DoctorSearchResponse;
import com.medislot.coreservice.dto.response.RecommendedDoctorResponse;
import com.medislot.coreservice.exception.AiResponseParsingException;
import com.medislot.coreservice.exception.AiServiceUnavailableException;
import com.medislot.coreservice.service.AiRecommendationService;
import com.medislot.coreservice.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AiRecommendationServiceImpl implements AiRecommendationService {

    private final RestClient groqRestClient;
    private final PromptBuilder promptBuilder;
    private final SearchService searchService;
    private final ObjectMapper objectMapper;

    @Value("${ms.groq.model}")
    private String model;

    private static final String MEDICAL_DISCLAIMER =
            "This is an AI-generated suggestion based on the symptoms described and is not a medical " +
                    "diagnosis. Please consult a qualified doctor for an accurate diagnosis and treatment.";

    @Override
    public RecommendedDoctorResponse getRecommendation(SymptomRequest request) {
        String rawResponse;
        try {
            rawResponse = groqRestClient.post()
                    .uri("/chat/completions")
                    .body(buildRequestBody(request.getSymptoms()))
                    .retrieve()
                    .body(String.class);
        } catch (RestClientException ex) {
            throw new AiServiceUnavailableException("AI service is currently unavailable. Please try again later.");
        }

        AiResult result = parseGroqResponse(rawResponse);

        List<DoctorSearchResponse> matchingDoctors = new ArrayList<>();
        Set<Long> seenDoctorIds = new HashSet<>();

        for (String specialization : result.specializations()) {
            SearchDoctorRequest searchRequest = new SearchDoctorRequest();
            searchRequest.setSpecialization(specialization);
            searchRequest.setPage(0);
            searchRequest.setSize(50);

            Page<DoctorSearchResponse> page = searchService.searchDoctors(searchRequest);
            for (DoctorSearchResponse doctor : page.getContent()) {
                if (seenDoctorIds.add(doctor.getId())) {
                    matchingDoctors.add(doctor);
                }
            }
        }

        return RecommendedDoctorResponse.builder()
                .recommendedSpecializations(result.specializations())
                .matchingDoctors(matchingDoctors)
                .confidence(result.confidence())
                .medicalDisclaimer(MEDICAL_DISCLAIMER)
                .build();
    }

    private String buildRequestBody(String symptoms) {
        try {
            Map<String, Object> systemMessage = Map.of("role", "system", "content", promptBuilder.buildSystemPrompt());
            Map<String, Object> userMessage = Map.of("role", "user", "content", promptBuilder.buildUserPrompt(symptoms));

            Map<String, Object> body = new LinkedHashMap<>();
            body.put("model", model);
            body.put("messages", List.of(systemMessage, userMessage));
            body.put("temperature", 0.2);
            body.put("response_format", Map.of("type", "json_object"));

            return objectMapper.writeValueAsString(body);
        } catch (JsonProcessingException ex) {
            throw new AiResponseParsingException("Failed to build AI request");
        }
    }

    private AiResult parseGroqResponse(String rawResponseBody) {
        try {
            JsonNode root = objectMapper.readTree(rawResponseBody);
            String content = root.path("choices").get(0).path("message").path("content").asText();

            JsonNode parsed = objectMapper.readTree(content);

            List<String> specializations = new ArrayList<>();
            parsed.path("specializations").forEach(node -> specializations.add(node.asText()));

            if (specializations.isEmpty()) {
                throw new AiResponseParsingException("AI did not return any specialization");
            }

            int confidence = parsed.path("confidence").asInt(0);
            return new AiResult(specializations, confidence);

        } catch (AiResponseParsingException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new AiResponseParsingException("AI returned an invalid response. Please try again.");
        }
    }

    private record AiResult(List<String> specializations, int confidence) {}
}
package com.medislot.coreservice.ai;

import org.springframework.stereotype.Component;

@Component
public class PromptBuilder {

    private static final String SYSTEM_PROMPT = """
            You are a medical specialization classifier for a healthcare appointment platform.
            You do not diagnose conditions or suggest treatment. You only map reported symptoms
            to the most relevant doctor specialization(s) from standard medical specializations
            (e.g. General Physician, Cardiologist, Pulmonologist, Orthopedic, Dermatologist,
            Neurologist, ENT Specialist, Gastroenterologist).
            Respond with ONLY a valid JSON object. No explanation, no markdown, no extra text.
            The JSON must strictly follow this exact shape:
            {"specializations": ["Specialization1", "Specialization2"], "confidence": 85}
            "confidence" is an integer from 0 to 100 representing your confidence in this mapping.
            Return at most 3 specializations, ordered by relevance.
            """;

    public String buildSystemPrompt() {
        return SYSTEM_PROMPT;
    }

    public String buildUserPrompt(String symptoms) {
        return "Patient reported symptoms: " + symptoms.trim();
    }
}
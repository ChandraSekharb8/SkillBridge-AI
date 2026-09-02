package com.skillbridge.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Service
public class GeminiService {

    @Value("${gemini.api.key:}")
    private String apiKey;

    @Value("${gemini.model:gemini-2.5-flash-lite}")
    private String model;

    public String generateContent(String prompt) {

        try {
            if (apiKey == null || apiKey.isBlank() || apiKey.equals("YOUR_GEMINI_API_KEY")) {
                return "AI service is not configured. Please add a valid Gemini API key.";
            }

            if (model == null || model.isBlank()) {
                model = "gemini-2.5-flash-lite";
            }

            String url = "https://generativelanguage.googleapis.com/v1beta/models/"
                    + model
                    + ":generateContent?key="
                    + apiKey;

            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> part = Map.of("text", prompt);
            Map<String, Object> content = Map.of("parts", List.of(part));
            Map<String, Object> requestBody = Map.of("contents", List.of(content));

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);

            Map body = response.getBody();

            if (body == null) {
                return "AI did not return a response. Please try again.";
            }

            Object candidatesObject = body.get("candidates");

            if (!(candidatesObject instanceof List)) {
                return "AI response format was unexpected. Please try again.";
            }

            List candidates = (List) candidatesObject;

            if (candidates.isEmpty()) {
                return "AI returned an empty response. Please try again.";
            }

            Object firstCandidateObject = candidates.get(0);

            if (!(firstCandidateObject instanceof Map)) {
                return "AI response could not be read properly. Please try again.";
            }

            Map firstCandidate = (Map) firstCandidateObject;
            Object contentObject = firstCandidate.get("content");

            if (!(contentObject instanceof Map)) {
                return "AI did not generate readable content. Please try again.";
            }

            Map contentMap = (Map) contentObject;
            Object partsObject = contentMap.get("parts");

            if (!(partsObject instanceof List)) {
                return "AI response text was not found. Please try again.";
            }

            List parts = (List) partsObject;

            if (parts.isEmpty()) {
                return "AI response text was empty. Please try again.";
            }

            Object firstPartObject = parts.get(0);

            if (!(firstPartObject instanceof Map)) {
                return "AI response text could not be read. Please try again.";
            }

            Map firstPart = (Map) firstPartObject;
            Object text = firstPart.get("text");

            if (text == null || text.toString().trim().isEmpty()) {
                return "AI generated an empty answer. Please try again.";
            }

            return text.toString().trim();

        } catch (HttpStatusCodeException e) {

            int statusCode = e.getStatusCode().value();

            if (statusCode == 400) {
                return "AI request failed. Please check the Gemini model name and API key configuration.";
            }

            if (statusCode == 401 || statusCode == 403) {
                return "AI authentication failed. Please check your Gemini API key.";
            }

            if (statusCode == 404) {
                return "AI model was not found. Please check GEMINI_MODEL value.";
            }

            if (statusCode == 429) {
                return "AI usage limit reached for now. Please wait for some time and try again.";
            }

            if (statusCode == 500 || statusCode == 502 || statusCode == 503 || statusCode == 504) {
                return "AI is busy right now. Please try again in a few minutes.";
            }

            return "AI service returned an error. Please try again.";

        } catch (ResourceAccessException e) {
            return "Unable to connect to AI service. Please check your internet connection and try again.";

        } catch (Exception e) {
            return "Something went wrong while generating AI response. Please try again.";
        }
    }
}
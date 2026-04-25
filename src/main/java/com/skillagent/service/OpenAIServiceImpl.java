package com.skillagent.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.*;

@Service
public class OpenAIServiceImpl implements OpenAIService {

    @Value("${openai.api.key:}") // ✅ SAFE (no crash if missing)
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public String generateLearningPlan(String resume, String jd, String gaps) {

        // ✅ If no key → safe fallback
        if (apiKey == null || apiKey.isEmpty()) {
            return fallback(gaps);
        }

        try {
            String prompt = "Missing skills: " + gaps +
                    ". Give a short learning plan (max 5 bullet points).";

            Map<String, Object> request = new HashMap<>();
            request.put("model", "gpt-4o-mini");
            request.put("input", prompt);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            HttpEntity<Map<String, Object>> entity =
                    new HttpEntity<>(request, headers);

            ResponseEntity<Map> response = restTemplate.postForEntity(
                    "https://api.openai.com/v1/responses",
                    entity,
                    Map.class
            );

            List output = (List) response.getBody().get("output");

            if (output == null || output.isEmpty()) {
                return fallback(gaps);
            }

            Map first = (Map) output.get(0);
            List content = (List) first.get("content");
            Map textObj = (Map) content.get(0);

            return textObj.get("text").toString();

        } catch (Exception e) {
            e.printStackTrace();
            return fallback(gaps);
        }
    }

    private String fallback(String gaps) {
        return "Learning Plan:\n" +
               "- Improve: " + gaps + "\n" +
               "- Build projects\n" +
               "- Practice real-world problems";
    }
}
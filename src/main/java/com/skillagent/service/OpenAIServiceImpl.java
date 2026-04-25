package com.skillagent.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.*;

@Service
public class OpenAIServiceImpl implements OpenAIService {

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public String generateLearningPlan(String resume, String jd, String gaps) {

        String prompt = "Missing skills: " + gaps +
                ". Generate a short learning plan (max 5 bullet points).";

        Map<String, Object> request = new HashMap<>();
        request.put("model", "gpt-3.5-turbo"); // ✅ stable model

        List<Map<String, String>> messages = new ArrayList<>();
        messages.add(Map.of(
                "role", "user",
                "content", prompt
        ));

        request.put("messages", messages);
        request.put("max_tokens", 150);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

        try {
            System.out.println("🔥 Calling OpenAI...");

            ResponseEntity<Map> response =
                    restTemplate.postForEntity(apiUrl, entity, Map.class);

            System.out.println("🔥 OpenAI response: " + response.getBody());

            List choices = (List) response.getBody().get("choices");

            if (choices == null || choices.isEmpty()) {
                return "❌ No AI response";
            }

            Map choice = (Map) choices.get(0);
            Map message = (Map) choice.get("message");

            return message.get("content").toString();

        } catch (Exception e) {
            e.printStackTrace(); // 🔥 VERY IMPORTANT
            return "❌ AI FAILED";
        }
    }
}
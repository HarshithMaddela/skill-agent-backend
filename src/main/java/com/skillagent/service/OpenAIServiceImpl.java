package com.skillagent.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class OpenAIServiceImpl implements OpenAIService {

    @Value("${openai.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public String callOpenAI(String prompt) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            Map<String, Object> body = new HashMap<>();
            body.put("model", "gpt-4o-mini");

            List<Map<String, String>> messages = new ArrayList<>();
            messages.add(Map.of("role", "system", "content", "Return only valid JSON."));
            messages.add(Map.of("role", "user", "content", prompt));

            body.put("messages", messages);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

            ResponseEntity<Map> response = restTemplate.postForEntity(
                    "https://api.openai.com/v1/chat/completions",
                    request,
                    Map.class
            );

            Map choice = (Map) ((List) response.getBody().get("choices")).get(0);
            Map message = (Map) choice.get("message");

            return message.get("content").toString();

        }  catch (Exception e) {
            e.printStackTrace();
            return "{}";
        }
    }
}
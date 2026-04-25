package com.skillagent.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

import java.util.*;

@Service
public class OpenAIServiceImpl implements OpenAIService {

    @Override
    public String generateLearningPlan(String resume, String jd, String gaps) {
        return "AI temporarily disabled.";
    }
}
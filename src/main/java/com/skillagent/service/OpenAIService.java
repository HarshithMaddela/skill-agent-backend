package com.skillagent.service;

public interface OpenAIService {
    String generateLearningPlan(String resume, String jd, String gaps);
}
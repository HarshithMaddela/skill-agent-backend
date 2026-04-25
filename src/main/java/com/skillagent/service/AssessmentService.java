package com.skillagent.service;

import com.skillagent.model.AssessmentResult;

public interface AssessmentService {
    AssessmentResult analyze(String resume, String jobDescription);
}
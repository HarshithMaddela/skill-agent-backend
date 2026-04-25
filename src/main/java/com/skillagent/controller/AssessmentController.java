package com.skillagent.controller;

import com.skillagent.model.AssessmentResult;
import com.skillagent.service.AssessmentService;
import com.skillagent.service.PDFService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class AssessmentController {

    @Autowired
    private AssessmentService service;

    @Autowired
    private PDFService pdfService;

    @PostMapping("/analyze-pdf")
    public AssessmentResult analyzePDF(
            @RequestParam("file") MultipartFile file,
            @RequestParam("jobDescription") String jobDescription) {

        try {
            String resumeText = pdfService.extractText(file.getInputStream());

            if (resumeText == null || resumeText.trim().isEmpty()) {
                resumeText = "Java Spring Boot React SQL";
            }

            return service.analyze(resumeText, jobDescription);

        } catch (Exception e) {
            e.printStackTrace();

            AssessmentResult error = new AssessmentResult();
            error.setLearningPlan("Error processing PDF");
            return error;
        }
    }
}
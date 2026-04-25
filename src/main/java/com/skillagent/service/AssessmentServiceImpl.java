package com.skillagent.service;

import com.skillagent.model.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AssessmentServiceImpl implements AssessmentService {

    @Override
    public AssessmentResult analyze(String resume, String jd) {

        List<String> skills = extractSkills(jd);
        List<Skill> results = new ArrayList<>();

        for (String skill : skills) {
            int score = evaluateSkill(skill, resume);

            String level;
            if (score >= 8) level = "Advanced";
            else if (score >= 5) level = "Intermediate";
            else level = "Beginner";

            results.add(new Skill(skill, score, level));
        }

        AssessmentResult res = new AssessmentResult();
        res.setSkills(results);
        res.setSkillGaps(getSkillGaps(results));
        res.setOverallFit(calculateOverallFit(results));
        res.setLearningPlan(generateLearningPlan(results)); // ⭐ UPDATED HERE

        return res;
    }

    // 🔍 SKILL EXTRACTION (MULTI-DOMAIN)
    private List<String> extractSkills(String jd) {

        List<String> allSkills = List.of(

                // 💻 IT
                "Java", "Spring Boot", "React", "Python",
                "Machine Learning", "Deep Learning",
                "SQL", "HTML", "CSS", "JavaScript",

                // ⚡ EEE
                "Power Systems", "Electrical Machines",
                "Control Systems", "Circuit Analysis",
                "MATLAB", "Simulink",
                "Transformers", "Transmission", "Distribution",
                "Protection Systems",

                // 📡 ECE
                "Digital Electronics", "Analog Electronics",
                "VLSI", "Signal Processing",
                "Communication Systems",
                "Embedded Systems", "Microcontrollers",
                "IoT", "FPGA",

                // 🏗️ CIVIL
                "Structural Engineering", "Geotechnical Engineering",
                "Fluid Mechanics", "Surveying",
                "Construction Management",
                "Concrete Technology", "Transportation Engineering"
        );

        List<String> extracted = new ArrayList<>();

        for (String skill : allSkills) {
            if (jd.toLowerCase().contains(skill.toLowerCase())) {
                extracted.add(skill);
            }
        }

        return extracted;
    }

    // 📊 SMART SCORING
    private int evaluateSkill(String skill, String resume) {

        String res = resume.toLowerCase();
        String sk = skill.toLowerCase();

        int occurrences = countOccurrences(res, sk);

        int score = 0;

        if (occurrences >= 3) score += 6;
        else if (occurrences == 2) score += 5;
        else if (occurrences == 1) score += 3;
        else score += 1;

        if (res.contains("project") && res.contains(sk)) score += 2;
        if (res.contains("internship") && res.contains(sk)) score += 1;

        // domain intelligence
        if (sk.equals("spring boot") && res.contains("java")) score += 1;
        if (sk.equals("react") && res.contains("javascript")) score += 1;
        if (sk.equals("embedded systems") && res.contains("microcontroller")) score += 1;

        return Math.min(score, 10);
    }

    private int countOccurrences(String text, String word) {
        int count = 0, index = 0;
        while ((index = text.indexOf(word, index)) != -1) {
            count++;
            index += word.length();
        }
        return count;
    }

    // ⚠ SKILL GAPS
    private List<String> getSkillGaps(List<Skill> skills) {

        List<String> gaps = new ArrayList<>();

        for (Skill s : skills) {
            if (s.getScore() <= 5) {
                gaps.add(s.getName());
            }
        }

        if (gaps.isEmpty()) {
            gaps.add("Advanced Domain Concepts");
        }

        return gaps;
    }

    // 🎯 OVERALL FIT
    private String calculateOverallFit(List<Skill> skills) {

        double avg = skills.stream()
                .mapToInt(Skill::getScore)
                .average()
                .orElse(0);

        if (avg >= 7) return "Strong Fit";
        else if (avg >= 5) return "Moderate Fit";
        else return "Needs Improvement";
    }

    // 🚀 🔥 FINAL SMART LEARNING PLAN
    private String generateLearningPlan(List<Skill> skills) {

        StringBuilder plan = new StringBuilder();
        plan.append("🚀 Personalized Learning Roadmap\n\n");

        // 🔥 sort weakest first
        skills.sort(Comparator.comparingInt(Skill::getScore));

        int limit = Math.min(5, skills.size());

        plan.append("📌 Priority Skills to Improve:\n");

        for (int i = 0; i < limit; i++) {
            Skill s = skills.get(i);

            if (s.getScore() <= 4) {
                plan.append("👉 ").append(s.getName())
                        .append(": Start from basics → practice daily → build mini project (5-7 days)\n");
            } 
            else if (s.getScore() <= 6) {
                plan.append("👉 ").append(s.getName())
                        .append(": Revise concepts + build 1 intermediate project (3-5 days)\n");
            } 
            else {
                plan.append("👉 ").append(s.getName())
                        .append(": Strengthen with advanced use-cases & optimization\n");
            }
        }

        plan.append("\n🛠 Practical Actions:\n");

        boolean isSoftware = skills.stream().anyMatch(s -> 
                s.getName().equalsIgnoreCase("Java") ||
                s.getName().equalsIgnoreCase("React") ||
                s.getName().equalsIgnoreCase("Python")
        );

        boolean isEEE = skills.stream().anyMatch(s ->
                s.getName().equalsIgnoreCase("Power Systems") ||
                s.getName().equalsIgnoreCase("Control Systems")
        );

        boolean isECE = skills.stream().anyMatch(s ->
                s.getName().equalsIgnoreCase("Embedded Systems") ||
                s.getName().equalsIgnoreCase("Signal Processing")
        );

        boolean isCivil = skills.stream().anyMatch(s ->
                s.getName().equalsIgnoreCase("Structural Engineering") ||
                s.getName().equalsIgnoreCase("Fluid Mechanics")
        );

        if (isSoftware) {
            plan.append("- Build 2 full-stack projects (React + Spring Boot)\n");
            plan.append("- Practice DSA (LeetCode)\n");
        }

        if (isEEE) {
            plan.append("- Simulate circuits using MATLAB/Simulink\n");
            plan.append("- Study real-world power system case studies\n");
        }

        if (isECE) {
            plan.append("- Build IoT/Embedded project using Arduino/ESP32\n");
            plan.append("- Work with sensors & microcontrollers\n");
        }

        if (isCivil) {
            plan.append("- Practice structural design problems\n");
            plan.append("- Work on AutoCAD/STAAD models\n");
        }

        plan.append("\n📈 Growth Strategy:\n");
        plan.append("- Study 2–3 hours daily\n");
        plan.append("- Focus on project-based learning\n");
        plan.append("- Revise weekly and track progress\n");

        return plan.toString();
    }
}
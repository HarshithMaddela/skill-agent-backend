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
        res.setLearningPlan(generateLearningPlan(results));

        return res;
    }

    // 🔥 MULTI-DOMAIN SKILL EXTRACTION
    private List<String> extractSkills(String jd) {

        List<String> allSkills = List.of(

                // 💻 IT / SOFTWARE
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

    // 📘 LEARNING PLAN
    private String generateLearningPlan(List<Skill> skills) {

        StringBuilder plan = new StringBuilder("Personalized Learning Plan:\n");

        for (Skill s : skills) {

            if (s.getScore() <= 5) {
                plan.append("- Improve ")
                        .append(s.getName())
                        .append(" (3-5 days)\n");
            }
            else if (s.getScore() <= 7) {
                plan.append("- Strengthen ")
                        .append(s.getName())
                        .append(" with advanced projects\n");
            }
        }

        plan.append("- Build domain-specific projects\n");
        plan.append("- Practice real-world problem solving\n");

        return plan.toString();
    }
}
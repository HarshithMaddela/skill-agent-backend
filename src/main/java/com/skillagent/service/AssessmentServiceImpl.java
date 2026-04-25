package com.skillagent.service;

import com.skillagent.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.*;

@Service
public class AssessmentServiceImpl implements AssessmentService {

    @Autowired
    private OpenAIService openAIService;

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

        List<String> gaps = getSkillGaps(results);

        AssessmentResult res = new AssessmentResult();
        res.setSkills(results);
        res.setSkillGaps(gaps);
        res.setOverallFit(calculateOverallFit(results));

        // 🔥 AI ONLY FOR LEARNING PLAN (LOW COST)
        String gapString = String.join(", ", gaps);
        String aiPlan = openAIService.generateLearningPlan(resume, jd, gapString);

        res.setLearningPlan(aiPlan);

        return res;
    }

    // 🚀 DYNAMIC SKILL EXTRACTION (NO HARD LIMIT)
    private List<String> extractSkills(String jd) {

        Set<String> extracted = new HashSet<>();

        // 1️⃣ Predefined core skills (important ones)
        List<String> coreSkills = List.of(
                "Java", "Spring Boot", "React", "Python",
                "SQL", "JavaScript", "HTML", "CSS",
                "Machine Learning", "Deep Learning"
        );

        for (String skill : coreSkills) {
            if (jd.toLowerCase().contains(skill.toLowerCase())) {
                extracted.add(skill);
            }
        }

        // 2️⃣ Dynamic extraction (capitalized words / tech words)
        Pattern pattern = Pattern.compile("\\b[A-Z][a-zA-Z+.#]{2,}\\b");
        Matcher matcher = pattern.matcher(jd);

        while (matcher.find()) {
            extracted.add(matcher.group());
        }

        return new ArrayList<>(extracted);
    }

    // 🧠 SMART SCORING (IMPROVED)
    private int evaluateSkill(String skill, String resume) {

        String res = resume.toLowerCase();
        String sk = skill.toLowerCase();

        int occurrences = countOccurrences(res, sk);

        int score = 0;

        // occurrence weight
        if (occurrences >= 3) score += 6;
        else if (occurrences == 2) score += 5;
        else if (occurrences == 1) score += 3;
        else score += 1;

        // context boost
        if (res.contains("project") && res.contains(sk)) score += 2;
        if (res.contains("internship") && res.contains(sk)) score += 1;

        // 🔥 synonym intelligence
        if (sk.equals("javascript") && res.contains("js")) score += 2;
        if (sk.equals("spring boot") && res.contains("spring")) score += 1;
        if (sk.equals("react") && res.contains("reactjs")) score += 1;

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
            gaps.add("Advanced Concepts");
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
}
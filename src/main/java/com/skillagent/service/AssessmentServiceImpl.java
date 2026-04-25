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

        if (resume == null) resume = "";
        if (jd == null) jd = "";

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

        // 🔥 AI learning plan (safe)
        String gapString = String.join(", ", gaps);
        String aiPlan = openAIService.generateLearningPlan(resume, jd, gapString);

        res.setLearningPlan(aiPlan);

        return res;
    }

    // 🚀 CLEAN SKILL EXTRACTION
    private List<String> extractSkills(String jd) {

        Set<String> extracted = new HashSet<>();

        List<String> coreSkills = List.of(
                "Java", "Spring Boot", "React", "Python",
                "SQL", "JavaScript", "HTML", "CSS",
                "Machine Learning", "Deep Learning",
                "Docker", "AWS", "Git", "Node.js", "REST"
        );

        for (String skill : coreSkills) {
            if (jd.toLowerCase().contains(skill.toLowerCase())) {
                extracted.add(skill);
            }
        }

        Pattern pattern = Pattern.compile("\\b[A-Z][a-zA-Z+.#]{2,}\\b");
        Matcher matcher = pattern.matcher(jd);

        while (matcher.find()) {
            String word = matcher.group();

            if (!isCommonWord(word)) {
                extracted.add(word);
            }
        }

        // 🔥 normalization
        Set<String> normalized = new HashSet<>();

        for (String skill : extracted) {
            String lower = skill.toLowerCase();

            if (lower.equals("spring") || lower.equals("boot")) continue;

            if (lower.equals("js")) normalized.add("JavaScript");
            else if (lower.equals("node")) normalized.add("Node.js");
            else normalized.add(skill);
        }

        return new ArrayList<>(normalized);
    }

    // 🔥 FILTER NOISE WORDS
    private boolean isCommonWord(String word) {
        List<String> ignore = List.of(
                "Looking", "Should", "Candidate", "Experience",
                "Knowledge", "Understanding", "Basic",
                "Developer", "Software", "Boot", "APIs",
                "System", "Application", "Role"
        );
        return ignore.contains(word);
    }

    // 🧠 IMPROVED SMART SCORING
    private int evaluateSkill(String skill, String resume) {

        String res = resume.toLowerCase();
        String sk = skill.toLowerCase();

        int score = 0;

        // ✅ direct match
        if (res.contains(sk)) score += 3;

        // ✅ partial/fuzzy match
        if (sk.contains("spring") && res.contains("spring")) score += 2;
        if (sk.contains("react") && res.contains("react")) score += 2;
        if (sk.contains("sql") &&
                (res.contains("mysql") || res.contains("postgres") || res.contains("sql")))
            score += 2;

        // ✅ synonyms
        if (sk.equals("javascript") && res.contains("js")) score += 2;
        if (sk.equals("node.js") && res.contains("node")) score += 2;
        if (sk.equals("rest") && res.contains("api")) score += 2;

        // ✅ context boost
        if (res.contains("project") && res.contains(sk)) score += 2;
        if (res.contains("internship") && res.contains(sk)) score += 1;

        // ✅ fallback fuzzy
        if (score == 0 && sk.length() >= 4 &&
                res.contains(sk.substring(0, 4))) {
            score += 2;
        }

        return Math.min(score, 10);
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
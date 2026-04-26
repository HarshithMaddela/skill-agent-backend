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

    // 🔍 SKILL EXTRACTION
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

    // 📊 SCORING
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

        if (gaps.isEmpty()) gaps.add("Advanced Domain Concepts");

        return gaps;
    }

    // 🎯 FIT
    private String calculateOverallFit(List<Skill> skills) {

        double avg = skills.stream()
                .mapToInt(Skill::getScore)
                .average()
                .orElse(0);

        if (avg >= 7) return "Strong Fit";
        else if (avg >= 5) return "Moderate Fit";
        else return "Needs Improvement";
    }

    // 🔥 RESOURCE CLASS
    static class SkillResource {
        String link;
        String duration;

        SkillResource(String link, String duration) {
            this.link = link;
            this.duration = duration;
        }
    }

    // 📚 FULL RESOURCE MAP
    private Map<String, SkillResource> getResources() {

        Map<String, SkillResource> map = new HashMap<>();

        // 💻 IT
        map.put("Java", new SkillResource("https://www.geeksforgeeks.org/java/", "2-3 weeks"));
        map.put("Spring Boot", new SkillResource("https://spring.io/guides", "1-2 weeks"));
        map.put("React", new SkillResource("https://react.dev/learn", "2 weeks"));
        map.put("Python", new SkillResource("https://www.learnpython.org/", "2 weeks"));
        map.put("Machine Learning", new SkillResource("https://www.coursera.org/learn/machine-learning", "4 weeks"));
        map.put("Deep Learning", new SkillResource("https://www.deeplearning.ai/", "4-6 weeks"));
        map.put("SQL", new SkillResource("https://www.w3schools.com/sql/", "1 week"));
        map.put("HTML", new SkillResource("https://www.w3schools.com/html/", "3-5 days"));
        map.put("CSS", new SkillResource("https://www.w3schools.com/css/", "1 week"));
        map.put("JavaScript", new SkillResource("https://javascript.info/", "2 weeks"));

        // ⚡ EEE
        map.put("Power Systems", new SkillResource("https://nptel.ac.in/courses/108104051", "3-4 weeks"));
        map.put("Electrical Machines", new SkillResource("https://nptel.ac.in/courses/108106072", "3 weeks"));
        map.put("Control Systems", new SkillResource("https://nptel.ac.in/courses/108106098", "3 weeks"));
        map.put("Circuit Analysis", new SkillResource("https://nptel.ac.in/courses/108102042", "2-3 weeks"));
        map.put("MATLAB", new SkillResource("https://matlabacademy.mathworks.com/", "1-2 weeks"));
        map.put("Simulink", new SkillResource("https://matlabacademy.mathworks.com/details/simulink-onramp", "1 week"));
        map.put("Transformers", new SkillResource("https://nptel.ac.in/courses/108106072", "1-2 weeks"));
        map.put("Transmission", new SkillResource("https://nptel.ac.in/courses/108105053", "2 weeks"));
        map.put("Distribution", new SkillResource("https://nptel.ac.in/courses/108105053", "2 weeks"));
        map.put("Protection Systems", new SkillResource("https://nptel.ac.in/courses/108101039", "2-3 weeks"));

        // 📡 ECE
        map.put("Digital Electronics", new SkillResource("https://nptel.ac.in/courses/117106086", "3 weeks"));
        map.put("Analog Electronics", new SkillResource("https://nptel.ac.in/courses/117106030", "3 weeks"));
        map.put("VLSI", new SkillResource("https://nptel.ac.in/courses/117106092", "4 weeks"));
        map.put("Signal Processing", new SkillResource("https://nptel.ac.in/courses/117102060", "3 weeks"));
        map.put("Communication Systems", new SkillResource("https://nptel.ac.in/courses/117101051", "3 weeks"));
        map.put("Embedded Systems", new SkillResource("https://www.coursera.org/specializations/embedded-systems", "4 weeks"));
        map.put("Microcontrollers", new SkillResource("https://www.geeksforgeeks.org/microcontroller/", "2 weeks"));
        map.put("IoT", new SkillResource("https://www.coursera.org/specializations/iot", "4 weeks"));
        map.put("FPGA", new SkillResource("https://www.udemy.com/course/fpga-design/", "3 weeks"));

        // 🏗️ CIVIL
        map.put("Structural Engineering", new SkillResource("https://nptel.ac.in/courses/105106113", "4 weeks"));
        map.put("Geotechnical Engineering", new SkillResource("https://nptel.ac.in/courses/105103097", "3 weeks"));
        map.put("Fluid Mechanics", new SkillResource("https://nptel.ac.in/courses/112105183", "3 weeks"));
        map.put("Surveying", new SkillResource("https://nptel.ac.in/courses/105104101", "2 weeks"));
        map.put("Construction Management", new SkillResource("https://nptel.ac.in/courses/105103093", "3 weeks"));
        map.put("Concrete Technology", new SkillResource("https://nptel.ac.in/courses/105102012", "2 weeks"));
        map.put("Transportation Engineering", new SkillResource("https://nptel.ac.in/courses/105104098", "3 weeks"));

        return map;
    }

    // 🚀 LEARNING PLAN
    private String generateLearningPlan(List<Skill> skills) {

        StringBuilder plan = new StringBuilder();
        Map<String, SkillResource> resources = getResources();

        plan.append("🚀 Personalized Learning Roadmap\n\n");

        skills.sort(Comparator.comparingInt(Skill::getScore));
        int limit = Math.min(5, skills.size());

        for (int i = 0; i < limit; i++) {

            Skill s = skills.get(i);
            SkillResource r = resources.getOrDefault(
                    s.getName(),
                    new SkillResource("https://www.google.com/search?q=" + s.getName(), "1-2 weeks")
            );

            plan.append("👉 ").append(s.getName()).append("\n");

            if (s.getScore() <= 4)
                plan.append("   Level: Beginner → Start basics + mini project\n");
            else if (s.getScore() <= 6)
                plan.append("   Level: Intermediate → Build project\n");
            else
                plan.append("   Level: Advanced → Optimize & master\n");

            plan.append("   📚 ").append(r.link).append("\n");
            plan.append("   ⏱ ").append(r.duration).append("\n\n");
        }

        return plan.toString();
    }
}
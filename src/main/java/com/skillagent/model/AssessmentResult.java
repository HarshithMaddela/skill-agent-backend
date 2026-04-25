package com.skillagent.model;

import java.util.List;

public class AssessmentResult {

    private List<Skill> skills;
    private List<String> skillGaps;
    private String overallFit;
    private String learningPlan;

    public List<Skill> getSkills() { return skills; }
    public void setSkills(List<Skill> skills) { this.skills = skills; }

    public List<String> getSkillGaps() { return skillGaps; }
    public void setSkillGaps(List<String> skillGaps) { this.skillGaps = skillGaps; }

    public String getOverallFit() { return overallFit; }
    public void setOverallFit(String overallFit) { this.overallFit = overallFit; }

    public String getLearningPlan() { return learningPlan; }
    public void setLearningPlan(String learningPlan) { this.learningPlan = learningPlan; }
}
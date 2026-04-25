package com.skillagent.model;

public class Skill {

    private String name;
    private int score;
    private String level;

    public Skill() {}

    public Skill(String name, int score, String level) {
        this.name = name;
        this.score = score;
        this.level = level;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }

    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }
}
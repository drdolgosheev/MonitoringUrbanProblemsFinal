package com.example.monitoringurbanproblems;

import java.util.List;

public class User {
    private String name = "";
    private String surname = "";
    private String mail = "";
    private int strikeCount = 0;
    private int problemCount = 0;
    private boolean isBaned = false;
    private boolean isModerator = false;
    private List<Problem> problems;

    public User(String name, String surname, String mail, int strikeCount, int problemCount,
                boolean isBaned, boolean isModerator, List<Problem> problems){
        this.name = name;
        this.surname = surname;
        this.mail = mail;
        this.strikeCount = strikeCount;
        this.problemCount = problemCount;
        this.isBaned = isBaned;
        this.isModerator = isModerator;
        this.problems = problems;
    }

    public String getMail() {
        return mail;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getStrikeCount() {
        return strikeCount;
    }

    public void setStrikeCount(int strikeCount) {
        this.strikeCount = strikeCount;
    }

    public int getProblemCount() {
        return problemCount;
    }

    public void setProblemCount(int problemCount) {
        this.problemCount = problemCount;
    }

    public boolean isBaned() {
        return isBaned;
    }

    public void setBaned(boolean baned) {
        isBaned = baned;
    }

    public boolean isModerator() {
        return isModerator;
    }

    public void setModerator(boolean moderator) {
        isModerator = moderator;
    }

    public List<Problem> getProblems() {
        return this.problems;
    }

    public void getProblems(List<Problem> problems) {
        this.problems = problems;
    }
}

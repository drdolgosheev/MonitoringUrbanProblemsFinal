package com.example.monitoringurbanproblems;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

public class User {
    private String name = "";
    private String surname = "";
    private String mail = "";
    private int strikeCount = 0;
    private int problemCount = 0;
    private boolean isBaned = false;
    private boolean isModerator = false;
    private boolean isAdmin = false;
    private List<Problem> problems;
    private String haveAva;

    public User(){}

    public User(String name, String surname, String mail, int strikeCount, int problemCount,
                boolean isBaned, boolean isModerator, List<Problem> problems, String haveAva, boolean isAdmin){
        this.name = name;
        this.surname = surname;
        this.mail = mail;
        this.strikeCount = strikeCount;
        this.problemCount = problemCount;
        this.isBaned = isBaned;
        this.isModerator = isModerator;
        this.problems = problems;
        this.haveAva = haveAva;
        this.isAdmin = isAdmin;
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

    public void setProblems(List<Problem> problems) {
        this.problems = problems;
    }

    public String isHaveAva() {
        return haveAva;
    }

    public void setHaveAva(String Ava) {
        this.haveAva = Ava;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}

package com.example.enigma.Models;

public class Leaderboard {

    private String rank;
    private String name;
    private String ques;
    private String score;

    public Leaderboard() {
    }

    public Leaderboard(String rank, String name, String ques, String score) {
        this.rank = rank;
        this.name = name;
        this.ques = ques;
        this.score = score;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQues() {
        return ques;
    }

    public void setQues(String ques) {
        this.ques = ques;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}

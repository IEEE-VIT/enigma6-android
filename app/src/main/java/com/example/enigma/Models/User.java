package com.example.enigma.Models;

public class User {

    private String name;
    private String email;
    private int points;
    private int level;
    private boolean[] usedHint;
    private int rank;


    public User() {
    }

    public User(String name, String email, int points, int level, boolean[] usedHint, int rank) {
        this.name = name;
        this.email = email;
        this.points = points;
        this.level = level;
        this.usedHint = usedHint;
        this.rank = rank;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean[] getUsedHint() {
        return usedHint;
    }

    public void setUsedHint(boolean[] usedHint) {
        this.usedHint = usedHint;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}

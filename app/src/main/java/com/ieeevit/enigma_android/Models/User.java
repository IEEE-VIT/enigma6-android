package com.ieeevit.enigma_android.Models;

public class User {

    private String name;
    private String email;
    private double points;
    private int level;
    private boolean[] usedHint;
    private int rank;
    private boolean isNameDefault;


    public User() {
    }

    public User(String name, String email, double points, int level, boolean[] usedHint, int rank, boolean isNameDefault) {
        this.name = name;
        this.email = email;
        this.points = points;
        this.level = level;
        this.usedHint = usedHint;
        this.rank = rank;
        this.isNameDefault = isNameDefault;
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

    public double getPoints() {
        return points;
    }

    public void setPoints(double points) {
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

    public boolean isNameDefault() {
        return isNameDefault;
    }

    public void setNameDefault(boolean nameDefault) {
        isNameDefault = nameDefault;
    }
}

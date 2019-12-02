package com.ieeevit.enigma_android.Models;

public class CurPlayer {

    private String name;
    private double points;
    private int rank;
    private int level;

    public CurPlayer() {
    }

    public CurPlayer(String name, double points, int rank, int level) {
        this.name = name;
        this.points = points;
        this.rank = rank;
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPoints() {
        return points;
    }

    public void setPoints(double points) {
        this.points = points;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}

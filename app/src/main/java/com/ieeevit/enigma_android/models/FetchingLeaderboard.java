package com.ieeevit.enigma_android.models;

public class FetchingLeaderboard {

    private int statusCode;
    private Payload payload;

    public FetchingLeaderboard() {
    }


    public FetchingLeaderboard(int statusCode, Payload payload) {
        this.statusCode = statusCode;
        this.payload = payload;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public Payload getPayload() {
        return payload;
    }

    public void setPayload(Payload payload) {
        this.payload = payload;
    }
}

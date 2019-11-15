package com.example.enigma.Models;

public class FetchingHint {

    private int statusCode;
    private Payload payload;
    private boolean wasHintUsed;

    public FetchingHint() {
    }

    public FetchingHint(int statusCode, Payload payload, boolean wasHintUsed) {
        this.statusCode = statusCode;
        this.payload = payload;
        this.wasHintUsed = wasHintUsed;
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

    public boolean isWasHintUsed() {
        return wasHintUsed;
    }

    public void setWasHintUsed(boolean wasHintUsed) {
        this.wasHintUsed = wasHintUsed;
    }
}

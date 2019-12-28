package com.ieeevit.enigma_android.models;

public class RegistrationResponse {

    private int statusCode;
    private Payload payload;
    private boolean wasUserRegistered;
    private boolean isRegSuccess;

    public RegistrationResponse() {
    }

    public RegistrationResponse(int statusCode, Payload payload, boolean wasUserRegistered, boolean isRegSuccess) {
        this.statusCode = statusCode;
        this.payload = payload;
        this.wasUserRegistered = wasUserRegistered;
        this.isRegSuccess = isRegSuccess;
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

    public boolean isWasUserRegistered() {
        return wasUserRegistered;
    }

    public void setWasUserRegistered(boolean wasUserRegistered) {
        this.wasUserRegistered = wasUserRegistered;
    }

    public boolean isRegSuccess() {
        return isRegSuccess;
    }

    public void setRegSuccess(boolean regSuccess) {
        isRegSuccess = regSuccess;
    }
}


package com.ieeevit.enigma_android.Models;

public class FetchingUserProfile {

    private int statusCode;
    private Payload payload;
    private String errorMsg;

    public FetchingUserProfile() {
    }

    public FetchingUserProfile(int statusCode, Payload payload, String errorMsg) {
        this.statusCode = statusCode;
        this.payload = payload;
        this.errorMsg = errorMsg;
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

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}

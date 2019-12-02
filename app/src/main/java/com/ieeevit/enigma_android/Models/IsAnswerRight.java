package com.ieeevit.enigma_android.Models;

public class IsAnswerRight {

    private int statusCode;
    private Payload payload;
    private boolean isAnswerCorrect;

    public IsAnswerRight() {
    }

    public IsAnswerRight(int statusCode, Payload payload, boolean isAnswerCorrect) {
        this.statusCode = statusCode;
        this.payload = payload;
        this.isAnswerCorrect = isAnswerCorrect;
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

    public boolean isAnswerCorrect() {
        return isAnswerCorrect;
    }

    public void setAnswerCorrect(boolean answerCorrect) {
        isAnswerCorrect = answerCorrect;
    }
}

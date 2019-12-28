package com.ieeevit.enigma_android.utils;

public class EnigmaStatus {

    private boolean hasStarted;
    private String primaryMessage;
    private String secondaryMessage;


    public EnigmaStatus() {
    }

    public EnigmaStatus(boolean hasStarted, String primaryMessage, String secondaryMessage) {
        this.hasStarted = hasStarted;
        this.primaryMessage = primaryMessage;
        this.secondaryMessage = secondaryMessage;
    }

    public boolean getHasStarted() {
        return hasStarted;
    }

    public void setHasStarted(boolean hasStarted) {
        this.hasStarted = hasStarted;
    }


    public String getPrimaryMessage() {
        return primaryMessage;
    }

    public void setPrimaryMessage(String primaryMessage) {
        this.primaryMessage = primaryMessage;
    }

    public String getSecondaryMessage() {
        return secondaryMessage;
    }

    public void setSecondaryMessage(String secondaryMessage) {
        this.secondaryMessage = secondaryMessage;
    }
}

package com.ieeevit.enigma_android;

public class EnigmaStatus {

    boolean hasStarted;

    public EnigmaStatus() {
    }

    public EnigmaStatus(boolean hasStarted) {
        this.hasStarted = hasStarted;
    }

    public boolean getHasStarted() {
        return hasStarted;
    }

    public void setHasStarted(boolean hasStarted) {
        this.hasStarted = hasStarted;
    }
}

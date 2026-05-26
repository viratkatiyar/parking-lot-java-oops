package com.parkinglot.model;

public abstract class Gate {
    private final String gateId;

    protected Gate(String gateId) {
        this.gateId = gateId;
    }

    public String getGateId() {
        return gateId;
    }
}

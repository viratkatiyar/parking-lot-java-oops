package com.parkinglot.model;

public abstract class Vehicle {
    private final String licensePlate;
    private final VehicleType type;
    private ParkingTicket ticket;

    protected Vehicle(String licensePlate, VehicleType type) {
        this.licensePlate = licensePlate;
        this.type = type;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public VehicleType getType() {
        return type;
    }

    public ParkingTicket getTicket() {
        return ticket;
    }

    public void setTicket(ParkingTicket ticket) {
        this.ticket = ticket;
    }

    @Override
    public String toString() {
        return type + " (" + licensePlate + ")";
    }
}

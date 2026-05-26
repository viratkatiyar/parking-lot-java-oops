package com.parkinglot.model;

import com.parkinglot.exception.ParkingLotException;

public abstract class ParkingSpot {
    private final String spotId;
    private final int floorNumber;
    private Vehicle parkedVehicle;

    protected ParkingSpot(String spotId, int floorNumber) {
        this.spotId = spotId;
        this.floorNumber = floorNumber;
    }

    public String getSpotId() {
        return spotId;
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public Vehicle getParkedVehicle() {
        return parkedVehicle;
    }

    public boolean isFree() {
        return parkedVehicle == null;
    }

    public abstract boolean canFit(Vehicle vehicle);

    public void park(Vehicle vehicle) {
        if (!isFree()) {
            throw new ParkingLotException("Spot " + spotId + " is already occupied!");
        }
        if (!canFit(vehicle)) {
            throw new ParkingLotException("Vehicle type " + vehicle.getType() + " does not fit in spot " + spotId);
        }
        this.parkedVehicle = vehicle;
    }

    public void unpark() {
        if (isFree()) {
            throw new ParkingLotException("Spot " + spotId + " is already empty!");
        }
        this.parkedVehicle = null;
    }
}

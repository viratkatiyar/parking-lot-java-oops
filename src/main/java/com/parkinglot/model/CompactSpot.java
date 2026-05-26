package com.parkinglot.model;

public class CompactSpot extends ParkingSpot {
    public CompactSpot(String spotId, int floorNumber) {
        super(spotId, floorNumber);
    }

    @Override
    public boolean canFit(Vehicle vehicle) {
        return vehicle.getType() == VehicleType.CAR || vehicle.getType() == VehicleType.MOTORCYCLE;
    }
}

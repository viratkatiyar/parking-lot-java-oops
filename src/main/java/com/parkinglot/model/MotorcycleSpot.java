package com.parkinglot.model;

public class MotorcycleSpot extends ParkingSpot {
    public MotorcycleSpot(String spotId, int floorNumber) {
        super(spotId, floorNumber);
    }

    @Override
    public boolean canFit(Vehicle vehicle) {
        return vehicle.getType() == VehicleType.MOTORCYCLE;
    }
}

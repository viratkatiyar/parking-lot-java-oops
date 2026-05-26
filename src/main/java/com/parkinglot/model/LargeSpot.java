package com.parkinglot.model;

public class LargeSpot extends ParkingSpot {
    public LargeSpot(String spotId, int floorNumber) {
        super(spotId, floorNumber);
    }

    @Override
    public boolean canFit(Vehicle vehicle) {
        // Large spots can accommodate any vehicle (Motorcycle, Car, Truck)
        return true;
    }
}

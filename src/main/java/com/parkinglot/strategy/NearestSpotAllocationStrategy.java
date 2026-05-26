package com.parkinglot.strategy;

import com.parkinglot.model.ParkingFloor;
import com.parkinglot.model.ParkingSpot;
import com.parkinglot.model.Vehicle;
import java.util.List;

public class NearestSpotAllocationStrategy implements SpotAllocationStrategy {
    @Override
    public ParkingSpot allocateSpot(Vehicle vehicle, List<ParkingFloor> floors) {
        // Iterate through floors sequentially (lowest floor first)
        for (ParkingFloor floor : floors) {
            // Find the first spot that is free and can accommodate this vehicle type
            for (ParkingSpot spot : floor.getSpots()) {
                if (spot.isFree() && spot.canFit(vehicle)) {
                    return spot;
                }
            }
        }
        return null; // No spot available
    }
}

package com.parkinglot.strategy;

import com.parkinglot.model.ParkingFloor;
import com.parkinglot.model.ParkingSpot;
import com.parkinglot.model.Vehicle;
import java.util.List;

public interface SpotAllocationStrategy {
    ParkingSpot allocateSpot(Vehicle vehicle, List<ParkingFloor> floors);
}

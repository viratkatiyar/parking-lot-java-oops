package com.parkinglot.model;

import com.parkinglot.exception.ParkingLotException;
import java.util.ArrayList;
import java.util.List;

public class ParkingFloor {
    private final int floorNumber;
    private final List<ParkingSpot> spots;
    private final DisplayBoard displayBoard;

    public ParkingFloor(int floorNumber) {
        this.floorNumber = floorNumber;
        this.spots = new ArrayList<>();
        this.displayBoard = new DisplayBoard("Floor-" + floorNumber + "-Board");
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public List<ParkingSpot> getSpots() {
        return spots;
    }

    public DisplayBoard getDisplayBoard() {
        return displayBoard;
    }

    public void addParkingSpot(ParkingSpot spot) {
        if (spot.getFloorNumber() != this.floorNumber) {
            throw new ParkingLotException("Spot floor number does not match this floor!");
        }
        spots.add(spot);
        updateDisplayBoard();
    }

    public void parkVehicle(Vehicle vehicle, ParkingSpot spot) {
        spot.park(vehicle);
        updateDisplayBoard();
    }

    public void unparkVehicle(ParkingSpot spot) {
        spot.unpark();
        updateDisplayBoard();
    }

    public void updateDisplayBoard() {
        int compactCount = 0;
        int motorcycleCount = 0;
        int largeCount = 0;

        for (ParkingSpot spot : spots) {
            if (spot.isFree()) {
                if (spot instanceof CompactSpot) {
                    compactCount++;
                } else if (spot instanceof MotorcycleSpot) {
                    motorcycleCount++;
                } else if (spot instanceof LargeSpot) {
                    largeCount++;
                }
            }
        }

        displayBoard.update("Compact Spots", compactCount);
        displayBoard.update("Motorcycle Spots", motorcycleCount);
        displayBoard.update("Large Spots", largeCount);
    }
}

package com.parkinglot.user;

import com.parkinglot.model.ParkingFloor;
import com.parkinglot.model.ParkingLot;
import com.parkinglot.model.ParkingSpot;

public class Admin extends Account {
    public Admin(String username, String password, String name, String email) {
        super(username, password, name, email);
    }

    public void addFloor(ParkingLot parkingLot, ParkingFloor floor) {
        parkingLot.addFloor(floor);
    }

    public void addParkingSpot(ParkingFloor floor, ParkingSpot spot) {
        floor.addParkingSpot(spot);
    }
}

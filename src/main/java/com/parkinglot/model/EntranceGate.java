package com.parkinglot.model;

import com.parkinglot.exception.ParkingLotException;

public class EntranceGate extends Gate {
    public EntranceGate(String gateId) {
        super(gateId);
    }

    public ParkingTicket issueTicket(Vehicle vehicle) {
        ParkingLot parkingLot = ParkingLot.getInstance();
        
        // Allocate spot using allocation strategy
        ParkingSpot spot = parkingLot.getAllocationStrategy()
                .allocateSpot(vehicle, parkingLot.getFloors());
        
        if (spot == null) {
            throw new ParkingLotException("Parking Full! No available spots for vehicle: " + vehicle);
        }

        // Park the vehicle in the allocated spot
        // Find the floor of the spot to trigger display board updates
        ParkingFloor floor = findFloor(parkingLot, spot.getFloorNumber());
        floor.parkVehicle(vehicle, spot);

        // Create and assign ticket
        ParkingTicket ticket = new ParkingTicket(spot);
        vehicle.setTicket(ticket);
        parkingLot.addTicket(ticket);

        System.out.println("Issued Ticket " + ticket.getTicketNumber() + " for spot " + spot.getSpotId());
        return ticket;
    }

    private ParkingFloor findFloor(ParkingLot parkingLot, int floorNum) {
        for (ParkingFloor floor : parkingLot.getFloors()) {
            if (floor.getFloorNumber() == floorNum) {
                return floor;
            }
        }
        throw new ParkingLotException("Floor " + floorNum + " not found!");
    }
}

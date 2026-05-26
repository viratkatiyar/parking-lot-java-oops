package com.parkinglot.model;

import com.parkinglot.exception.ParkingLotException;
import com.parkinglot.payment.Payment;
import com.parkinglot.payment.PaymentStatus;

public class ExitGate extends Gate {
    public ExitGate(String gateId) {
        super(gateId);
    }

    public double calculateFee(ParkingTicket ticket) {
        return calculateFee(ticket, System.currentTimeMillis());
    }

    public double calculateFee(ParkingTicket ticket, long simulatedExitTime) {
        if (ticket.getStatus() == TicketStatus.PAID) {
            return 0.0;
        }
        ParkingLot parkingLot = ParkingLot.getInstance();
        ParkingSpot spot = ticket.getAllocatedSpot();
        Vehicle vehicle = spot.getParkedVehicle();
        if (vehicle == null) {
            throw new ParkingLotException("No vehicle found at the spot assigned to ticket: " + ticket.getTicketNumber());
        }
        double duration = ticket.getDurationInHours(simulatedExitTime);
        return parkingLot.getFeeStrategy().calculateFee(duration, vehicle.getType());
    }

    public boolean processPayment(ParkingTicket ticket, Payment payment) {
        return processPayment(ticket, payment, System.currentTimeMillis());
    }

    public boolean processPayment(ParkingTicket ticket, Payment payment, long simulatedExitTime) {
        if (ticket.getStatus() == TicketStatus.PAID) {
            throw new ParkingLotException("Ticket " + ticket.getTicketNumber() + " is already paid!");
        }

        // Process transaction
        boolean isSuccess = payment.process();
        if (isSuccess && payment.getStatus() == PaymentStatus.COMPLETED) {
            ticket.markPaid(payment.getAmount(), simulatedExitTime);

            // Free the spot
            ParkingSpot spot = ticket.getAllocatedSpot();
            Vehicle vehicle = spot.getParkedVehicle();

            ParkingLot parkingLot = ParkingLot.getInstance();
            parkingLot.addPayment(payment);
            ParkingFloor floor = findFloor(parkingLot, spot.getFloorNumber());
            
            if (vehicle != null) {
                vehicle.setTicket(null);
            }
            floor.unparkVehicle(spot);
            
            System.out.println("Payment successful. Spot " + spot.getSpotId() + " is now free.");
            return true;
        }

        System.out.println("Payment failed for ticket " + ticket.getTicketNumber());
        return false;
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

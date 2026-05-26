package com.parkinglot.model;

import com.parkinglot.exception.ParkingLotException;
import com.parkinglot.strategy.FeeCalculationStrategy;
import com.parkinglot.strategy.FlatHourlyFeeStrategy;
import com.parkinglot.strategy.NearestSpotAllocationStrategy;
import com.parkinglot.strategy.SpotAllocationStrategy;
import com.parkinglot.payment.Payment;
import java.util.ArrayList;
import java.util.List;

public class ParkingLot {
    private static ParkingLot instance;

    private final String name;
    private final String address;
    private final List<ParkingFloor> floors;
    private final List<EntranceGate> entranceGates;
    private final List<ExitGate> exitGates;
    private final List<ParkingTicket> tickets;
    private final List<Payment> payments;
    private SpotAllocationStrategy allocationStrategy;
    private FeeCalculationStrategy feeStrategy;

    private ParkingLot(String name, String address) {
        this.name = name;
        this.address = address;
        this.floors = new ArrayList<>();
        this.entranceGates = new ArrayList<>();
        this.exitGates = new ArrayList<>();
        this.tickets = new ArrayList<>();
        this.payments = new ArrayList<>();
        this.allocationStrategy = new NearestSpotAllocationStrategy();
        this.feeStrategy = new FlatHourlyFeeStrategy();
    }

    public static synchronized ParkingLot initialize(String name, String address) {
        if (instance == null) {
            instance = new ParkingLot(name, address);
        }
        return instance;
    }

    public static synchronized ParkingLot getInstance() {
        if (instance == null) {
            throw new ParkingLotException("Parking Lot is not initialized! Call initialize first.");
        }
        return instance;
    }

    // Helper to reset instance (useful for unit tests)
    public static synchronized void resetInstance() {
        instance = null;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public List<ParkingFloor> getFloors() {
        return floors;
    }

    public List<EntranceGate> getEntranceGates() {
        return entranceGates;
    }

    public List<ExitGate> getExitGates() {
        return exitGates;
    }

    public SpotAllocationStrategy getAllocationStrategy() {
        return allocationStrategy;
    }

    public void setAllocationStrategy(SpotAllocationStrategy allocationStrategy) {
        this.allocationStrategy = allocationStrategy;
    }

    public FeeCalculationStrategy getFeeStrategy() {
        return feeStrategy;
    }

    public void setFeeStrategy(FeeCalculationStrategy feeStrategy) {
        this.feeStrategy = feeStrategy;
    }

    public void addFloor(ParkingFloor floor) {
        // Ensure floor number is unique
        for (ParkingFloor existingFloor : floors) {
            if (existingFloor.getFloorNumber() == floor.getFloorNumber()) {
                throw new ParkingLotException("Floor " + floor.getFloorNumber() + " already exists!");
            }
        }
        floors.add(floor);
    }

    public void addEntranceGate(EntranceGate gate) {
        entranceGates.add(gate);
    }

    public void addExitGate(ExitGate gate) {
        exitGates.add(gate);
    }

    public List<ParkingTicket> getTickets() {
        return tickets;
    }

    public void addTicket(ParkingTicket ticket) {
        tickets.add(ticket);
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void addPayment(Payment payment) {
        payments.add(payment);
    }
}

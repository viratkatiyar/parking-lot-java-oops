package com.parkinglot.model;

import java.util.UUID;

public class ParkingTicket {
    private final String ticketNumber;
    private final ParkingSpot allocatedSpot;
    private final long entryTime;
    private long exitTime;
    private double fee;
    private TicketStatus status;

    public ParkingTicket(ParkingSpot allocatedSpot) {
        this.ticketNumber = "TCK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.allocatedSpot = allocatedSpot;
        this.entryTime = System.currentTimeMillis();
        this.status = TicketStatus.ACTIVE;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public ParkingSpot getAllocatedSpot() {
        return allocatedSpot;
    }

    public long getEntryTime() {
        return entryTime;
    }

    public long getExitTime() {
        return exitTime;
    }

    public double getFee() {
        return fee;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    public void markPaid(double fee, long exitTime) {
        this.fee = fee;
        this.exitTime = exitTime;
        this.status = TicketStatus.PAID;
    }

    public double getDurationInHours() {
        long endTime = (exitTime == 0) ? System.currentTimeMillis() : exitTime;
        long durationMs = endTime - entryTime;
        // Convert to hours (1 hour = 3600000 ms)
        return (double) durationMs / 3600000.0;
    }

    public double getDurationInHours(long simulatedExitTime) {
        long durationMs = simulatedExitTime - entryTime;
        return (double) durationMs / 3600000.0;
    }
}

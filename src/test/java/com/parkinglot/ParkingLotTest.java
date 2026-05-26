package com.parkinglot;

import com.parkinglot.exception.ParkingLotException;
import com.parkinglot.model.*;
import com.parkinglot.payment.CardPayment;
import com.parkinglot.payment.Payment;
import com.parkinglot.strategy.FlatHourlyFeeStrategy;
import com.parkinglot.strategy.NearestSpotAllocationStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ParkingLotTest {

    private ParkingLot parkingLot;
    private EntranceGate entranceGate;
    private ExitGate exitGate;

    @BeforeEach
    public void setUp() {
        // Reset singleton state to ensure tests are isolated
        ParkingLot.resetInstance();
        
        // Initialize parking lot
        parkingLot = ParkingLot.initialize("Test Garage", "100 Test St");
        
        // Add 1 floor with 3 spots of different types
        ParkingFloor floor = new ParkingFloor(1);
        floor.addParkingSpot(new MotorcycleSpot("F1-M1", 1));
        floor.addParkingSpot(new CompactSpot("F1-C1", 1));
        floor.addParkingSpot(new LargeSpot("F1-L1", 1));
        parkingLot.addFloor(floor);
        
        // Add gates
        entranceGate = new EntranceGate("Entrance-1");
        exitGate = new ExitGate("Exit-1");
        parkingLot.addEntranceGate(entranceGate);
        parkingLot.addExitGate(exitGate);
    }

    @Test
    public void testSingletonInstance() {
        ParkingLot instance1 = ParkingLot.getInstance();
        ParkingLot instance2 = ParkingLot.getInstance();
        assertSame(instance1, instance2);
    }

    @Test
    public void testSpotAllocationNearestCorrectType() {
        Vehicle motorcycle = new Motorcycle("MOTO-1");
        Vehicle car = new Car("CAR-1");
        Vehicle truck = new Truck("TRUCK-1");

        // Motorcycle should go to Motorcycle Spot (F1-M1) as it is the first matching spot
        ParkingTicket t1 = entranceGate.issueTicket(motorcycle);
        assertEquals("F1-M1", t1.getAllocatedSpot().getSpotId());

        // Car should go to Compact Spot (F1-C1)
        ParkingTicket t2 = entranceGate.issueTicket(car);
        assertEquals("F1-C1", t2.getAllocatedSpot().getSpotId());

        // Truck should go to Large Spot (F1-L1)
        ParkingTicket t3 = entranceGate.issueTicket(truck);
        assertEquals("F1-L1", t3.getAllocatedSpot().getSpotId());
    }

    @Test
    public void testCompactSpotFallsBackToLargeIfFull() {
        // Motorcycle goes to Motorcycle Spot
        entranceGate.issueTicket(new Motorcycle("MOTO-1"));
        
        // Car-1 goes to Compact Spot (F1-C1)
        entranceGate.issueTicket(new Car("CAR-1"));

        // Car-2 also needs a spot. F1-C1 is occupied, so it should fall back to Large Spot (F1-L1) since Large can fit Cars!
        ParkingTicket ticket = entranceGate.issueTicket(new Car("CAR-2"));
        assertEquals("F1-L1", ticket.getAllocatedSpot().getSpotId());
    }

    @Test
    public void testParkingFullThrowsException() {
        // Fill all three spots
        entranceGate.issueTicket(new Motorcycle("MOTO-1"));
        entranceGate.issueTicket(new Car("CAR-1"));
        entranceGate.issueTicket(new Truck("TRUCK-1"));

        // Fourth vehicle should trigger a ParkingLotException as no spots are available
        assertThrows(ParkingLotException.class, () -> {
            entranceGate.issueTicket(new Car("CAR-2"));
        });
    }

    @Test
    public void testFeeCalculationRounding() {
        Vehicle car = new Car("CAR-IN");
        ParkingTicket ticket = entranceGate.issueTicket(car);

        // Simulation duration: 2.5 hours -> should round up to 3 hours.
        // Rate for Car is $4/hour -> Fee should be 3 * 4 = $12
        long simulatedExitTime = ticket.getEntryTime() + (long) (2.5 * 3600000.0);
        double fee = exitGate.calculateFee(ticket, simulatedExitTime);

        assertEquals(12.0, fee, 0.001);
    }

    @Test
    public void testPaymentAndCheckoutFlow() {
        Vehicle truck = new Truck("TRUCK-F");
        ParkingTicket ticket = entranceGate.issueTicket(truck);

        long simulatedExitTime = ticket.getEntryTime() + (long) (1.2 * 3600000.0); // rounds up to 2 hours
        double fee = exitGate.calculateFee(ticket, simulatedExitTime); // 2 hours * $10 = $20

        assertEquals(20.0, fee, 0.001);

        Payment payment = new CardPayment(fee, "1234567812345678", "Virat Kohli");
        boolean checkoutSuccess = exitGate.processPayment(ticket, payment, simulatedExitTime);

        assertTrue(checkoutSuccess);
        assertEquals(TicketStatus.PAID, ticket.getStatus());
        assertTrue(ticket.getAllocatedSpot().isFree());
    }
}

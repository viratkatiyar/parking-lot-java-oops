package com.parkinglot;

import com.parkinglot.exception.ParkingLotException;
import com.parkinglot.model.*;
import com.parkinglot.payment.CardPayment;
import com.parkinglot.payment.CashPayment;
import com.parkinglot.payment.Payment;
import com.parkinglot.strategy.NearestSpotAllocationStrategy;
import com.parkinglot.strategy.FlatHourlyFeeStrategy;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static ParkingLot parkingLot;

    public static void main(String[] args) {
        // Initialize ParkingLot
        parkingLot = ParkingLot.initialize("Aura Grand Parking Plaza", "777 Skyline Boulevard");

        // Set up initial configuration
        setupDefaultParkingLot();

        System.out.println("==================================================================");
        System.out.println("        WELCOME TO " + parkingLot.getName().toUpperCase());
        System.out.println("        Address: " + parkingLot.getAddress());
        System.out.println("==================================================================");

        boolean running = true;
        while (running) {
            printMainMenu();
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    parkVehicleMenu();
                    break;
                case "2":
                    checkoutVehicleMenu();
                    break;
                case "3":
                    viewParkingLotMap();
                    break;
                case "4":
                    viewDisplayBoards();
                    break;
                case "5":
                    viewAdminReport();
                    break;
                case "6":
                    running = false;
                    System.out.println("\nThank you for using " + parkingLot.getName() + ". Goodbye!");
                    break;
                default:
                    System.out.println("\n[ERROR] Invalid choice. Please enter a number between 1 and 6.");
            }
        }
    }

    private static void setupDefaultParkingLot() {
        // Add 2 floors
        ParkingFloor floor1 = new ParkingFloor(1);
        ParkingFloor floor2 = new ParkingFloor(2);

        // Floor 1 Spots: 2 Motorcycle, 3 Compact, 2 Large
        floor1.addParkingSpot(new MotorcycleSpot("F1-M101", 1));
        floor1.addParkingSpot(new MotorcycleSpot("F1-M102", 1));
        floor1.addParkingSpot(new CompactSpot("F1-C103", 1));
        floor1.addParkingSpot(new CompactSpot("F1-C104", 1));
        floor1.addParkingSpot(new CompactSpot("F1-C105", 1));
        floor1.addParkingSpot(new LargeSpot("F1-L106", 1));
        floor1.addParkingSpot(new LargeSpot("F1-L107", 1));

        // Floor 2 Spots: 2 Motorcycle, 2 Compact, 2 Large
        floor2.addParkingSpot(new MotorcycleSpot("F2-M201", 2));
        floor2.addParkingSpot(new MotorcycleSpot("F2-M202", 2));
        floor2.addParkingSpot(new CompactSpot("F2-C203", 2));
        floor2.addParkingSpot(new CompactSpot("F2-C204", 2));
        floor2.addParkingSpot(new LargeSpot("F2-L205", 2));
        floor2.addParkingSpot(new LargeSpot("F2-L206", 2));

        parkingLot.addFloor(floor1);
        parkingLot.addFloor(floor2);

        // Add gates
        parkingLot.addEntranceGate(new EntranceGate("Entrance-Gate-A"));
        parkingLot.addEntranceGate(new EntranceGate("Entrance-Gate-B"));
        parkingLot.addExitGate(new ExitGate("Exit-Gate-X"));
        parkingLot.addExitGate(new ExitGate("Exit-Gate-Y"));
    }

    private static void printMainMenu() {
        System.out.println("\n------------------------------------------------------------------");
        System.out.println("   MAIN MENU");
        System.out.println("------------------------------------------------------------------");
        System.out.println("1. Park a Vehicle (Check-in)");
        System.out.println("2. Unpark a Vehicle (Check-out & Payment)");
        System.out.println("3. View Parking Lot Map (Live Grid Layout)");
        System.out.println("4. View Floor Display Boards");
        System.out.println("5. View Admin Report & Transaction Ledgers");
        System.out.println("6. Exit System");
        System.out.print("Select an option (1-6): ");
    }

    private static void parkVehicleMenu() {
        System.out.println("\n--- PARK A VEHICLE ---");
        System.out.print("Enter License Plate: ");
        String license = scanner.nextLine().trim().toUpperCase();
        if (license.isEmpty()) {
            System.out.println("[ERROR] License plate cannot be empty.");
            return;
        }

        System.out.println("Select Vehicle Type:");
        System.out.println("1. Motorcycle");
        System.out.println("2. Car (Compact)");
        System.out.println("3. Truck (Large)");
        System.out.print("Enter choice (1-3): ");
        String typeChoice = scanner.nextLine().trim();

        Vehicle vehicle;
        switch (typeChoice) {
            case "1":
                vehicle = new Motorcycle(license);
                break;
            case "2":
                vehicle = new Car(license);
                break;
            case "3":
                vehicle = new Truck(license);
                break;
            default:
                System.out.println("[ERROR] Invalid vehicle type selection.");
                return;
        }

        // Select entrance gate
        List<EntranceGate> gates = parkingLot.getEntranceGates();
        System.out.println("Select Entrance Gate:");
        for (int i = 0; i < gates.size(); i++) {
            System.out.println((i + 1) + ". " + gates.get(i).getGateId());
        }
        System.out.print("Enter choice: ");
        int gateIndex;
        try {
            gateIndex = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (gateIndex < 0 || gateIndex >= gates.size()) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            System.out.println("[ERROR] Invalid gate choice.");
            return;
        }

        EntranceGate gate = gates.get(gateIndex);

        try {
            ParkingTicket ticket = gate.issueTicket(vehicle);
            System.out.println("\n[SUCCESS] Vehicle parked successfully!");
            System.out.println("Ticket Details:");
            System.out.println(" - Ticket ID     : " + ticket.getTicketNumber());
            System.out.println(" - Allocated Spot: " + ticket.getAllocatedSpot().getSpotId());
            System.out.println(" - Entry Time    : " + new java.util.Date(ticket.getEntryTime()));
        } catch (ParkingLotException e) {
            System.out.println("\n[DENIED] " + e.getMessage());
        }
    }

    private static void checkoutVehicleMenu() {
        System.out.println("\n--- UNPARK A VEHICLE (CHECK-OUT) ---");
        System.out.print("Enter Ticket ID or License Plate: ");
        String searchKey = scanner.nextLine().trim().toUpperCase();

        if (searchKey.isEmpty()) {
            System.out.println("[ERROR] Search key cannot be empty.");
            return;
        }

        // Locate ticket
        ParkingTicket targetTicket = null;
        for (ParkingTicket ticket : parkingLot.getTickets()) {
            if (ticket.getTicketNumber().equalsIgnoreCase(searchKey)) {
                targetTicket = ticket;
                break;
            }
            // Check matching parked vehicle license plate
            ParkingSpot spot = ticket.getAllocatedSpot();
            Vehicle vehicle = spot.getParkedVehicle();
            if (vehicle != null && vehicle.getLicensePlate().equalsIgnoreCase(searchKey)) {
                targetTicket = ticket;
                break;
            }
        }

        if (targetTicket == null) {
            System.out.println("[ERROR] No active parking ticket found for: " + searchKey);
            return;
        }

        if (targetTicket.getStatus() == TicketStatus.PAID) {
            System.out.println("[WARNING] Ticket " + targetTicket.getTicketNumber() + " is already paid.");
            return;
        }

        ParkingSpot spot = targetTicket.getAllocatedSpot();
        Vehicle vehicle = spot.getParkedVehicle();
        if (vehicle == null) {
            System.out.println("[ERROR] Critical inconsistency: No vehicle found in spot " + spot.getSpotId());
            return;
        }

        // Prompt for duration to simulate time passing
        System.out.print("Enter simulated hours parked (e.g. 0.5, 2.0, 5.5): ");
        double hours;
        try {
            hours = Double.parseDouble(scanner.nextLine().trim());
            if (hours < 0) {
                System.out.println("[ERROR] Hours cannot be negative.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("[ERROR] Invalid numeric format.");
            return;
        }

        long entryTime = targetTicket.getEntryTime();
        long simulatedExitTime = entryTime + (long) (hours * 3600000.0);

        // Select exit gate
        List<ExitGate> exitGates = parkingLot.getExitGates();
        System.out.println("Select Exit Gate:");
        for (int i = 0; i < exitGates.size(); i++) {
            System.out.println((i + 1) + ". " + exitGates.get(i).getGateId());
        }
        System.out.print("Enter choice: ");
        int gateIndex;
        try {
            gateIndex = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (gateIndex < 0 || gateIndex >= exitGates.size()) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            System.out.println("[ERROR] Invalid gate choice.");
            return;
        }
        ExitGate gate = exitGates.get(gateIndex);

        double fee = gate.calculateFee(targetTicket, simulatedExitTime);
        System.out.println("\nBilling Invoice:");
        System.out.println(" - Vehicle Info  : " + vehicle);
        System.out.println(" - Parking Spot  : " + spot.getSpotId());
        System.out.printf(" - Duration      : %.2f hours\n", hours);
        System.out.printf(" - Total Fare    : $%.2f\n", fee);

        System.out.println("\nSelect Payment Method:");
        System.out.println("1. Credit/Debit Card");
        System.out.println("2. Cash");
        System.out.print("Enter choice (1-2): ");
        String payChoice = scanner.nextLine().trim();

        Payment payment = null;
        if (payChoice.equals("1")) {
            System.out.print("Enter Card Number: ");
            String card = scanner.nextLine().trim();
            System.out.print("Enter Cardholder Name: ");
            String name = scanner.nextLine().trim();
            payment = new CardPayment(fee, card, name);
        } else if (payChoice.equals("2")) {
            System.out.printf("Enter cash amount tendered (Must be >= $%.2f): ", fee);
            double cash;
            try {
                cash = Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("[ERROR] Invalid cash format.");
                return;
            }
            try {
                payment = new CashPayment(fee, cash);
            } catch (ParkingLotException e) {
                System.out.println("[ERROR] " + e.getMessage());
                return;
            }
        } else {
            System.out.println("[ERROR] Invalid payment method choice.");
            return;
        }

        // Process payment and unpark
        boolean success = gate.processPayment(targetTicket, payment, simulatedExitTime);
        if (success) {
            System.out.println("[SUCCESS] Receipt Printed. Vehicle unparked successfully.");
        } else {
            System.out.println("[ERROR] Checkout aborted due to payment failure.");
        }
    }

    private static void viewParkingLotMap() {
        System.out.println("\n==================================================================");
        System.out.println("   LIVE PARKING LOT SPOT GRID MAP");
        System.out.println("==================================================================");
        for (ParkingFloor floor : parkingLot.getFloors()) {
            System.out.println("\nFLOOR " + floor.getFloorNumber() + ":");
            System.out.println("-------------------------------------");
            List<ParkingSpot> spots = floor.getSpots();
            
            // Format spots layout visually
            for (ParkingSpot spot : spots) {
                String spotTypeInitial = spot.getClass().getSimpleName().substring(0, 1);
                String statusString;
                if (spot.isFree()) {
                    statusString = "FREE";
                } else {
                    statusString = spot.getParkedVehicle().getLicensePlate();
                }
                System.out.printf("[%s: %s | %s]  ", spot.getSpotId(), spotTypeInitial, statusString);
                
                // Print a new line every 3 spots for neatness
                if (spots.indexOf(spot) % 3 == 2) {
                    System.out.println();
                }
            }
            if (spots.size() % 3 != 0) {
                System.out.println();
            }
            System.out.println("-------------------------------------");
            System.out.println("Key: [SpotID: Type | Status/License]");
            System.out.println("Type: M = Motorcycle, C = Compact (Car), L = Large (Truck)");
        }
        System.out.println("==================================================================");
    }

    private static void viewDisplayBoards() {
        System.out.println("\n--- FLOOR STATUS DISPLAY BOARDS ---");
        for (ParkingFloor floor : parkingLot.getFloors()) {
            floor.getDisplayBoard().printDisplay();
            System.out.println();
        }
    }

    private static void viewAdminReport() {
        System.out.println("\n==================================================================");
        System.out.println("   ADMIN MANAGEMENT & AUDITING REPORT");
        System.out.println("==================================================================");
        
        List<ParkingTicket> allTickets = parkingLot.getTickets();
        long activeCount = allTickets.stream().filter(t -> t.getStatus() == TicketStatus.ACTIVE).count();
        long paidCount = allTickets.stream().filter(t -> t.getStatus() == TicketStatus.PAID).count();
        
        double totalRevenue = parkingLot.getPayments().stream()
                .mapToDouble(Payment::getAmount)
                .sum();

        System.out.println("General Metrics:");
        System.out.println(" - Total Tickets Issued  : " + allTickets.size());
        System.out.println(" - Active Vehicles Parked: " + activeCount);
        System.out.println(" - Completed Checkouts   : " + paidCount);
        System.out.printf(" - Total Revenue Earned  : $%.2f\n", totalRevenue);
        
        System.out.println("\nTransaction Ledgers (Payments):");
        System.out.println("------------------------------------------------------------------");
        System.out.printf("%-15s | %-12s | %-8s | %-10s\n", "Transaction ID", "Status", "Amount", "Method");
        System.out.println("------------------------------------------------------------------");
        for (Payment pay : parkingLot.getPayments()) {
            String method = pay.getClass().getSimpleName().replace("Payment", "");
            System.out.printf("%-15s | %-12s | $%-7.2f | %-10s\n", 
                    pay.getTxnId(), pay.getStatus(), pay.getAmount(), method);
        }
        if (parkingLot.getPayments().isEmpty()) {
            System.out.println("   No completed transactions.");
        }
        System.out.println("==================================================================");
    }
}

# OOP-Based Parking Lot System in Java

A clean, robust, and extensible console-based **Parking Lot Management System** built in pure Java. The project is designed from the ground up utilizing Core Object-Oriented Programming (OOP) concepts, SOLID principles, and industry-standard design patterns.

---

## 🏗️ Architecture & Class Design

The project is structured with high cohesion and low coupling in mind. The domain model classes are separated from business logic strategies, payment systems, and interactive interfaces.

```
src/main/java/com/parkinglot/
├── Main.java                        # Interactive CLI Simulation
├── exception/
│   └── ParkingLotException.java     # Custom Domain Exception
├── model/
│   ├── Vehicle.java                 # Abstract Vehicle Model
│   │   ├── Car.java
│   │   ├── Motorcycle.java
│   │   └── Truck.java
│   ├── ParkingSpot.java             # Abstract Parking Spot Model
│   │   ├── CompactSpot.java
│   │   ├── MotorcycleSpot.java
│   │   └── LargeSpot.java
│   ├── ParkingFloor.java            # Represents a Floor containing Spots
│   ├── DisplayBoard.java            # Real-time floor occupancy board
│   ├── ParkingTicket.java           # Parking token issued upon entry
│   ├── TicketStatus.java            # ACTIVE, PAID, LOST
│   ├── Gate.java                    # Base Gate representation
│   │   ├── EntranceGate.java        # Issues tickets and parks vehicles
│   │   └── ExitGate.java            # Scans tickets, bills, and processes checkouts
│   └── VehicleType.java             # MOTORCYCLE, CAR, TRUCK
├── payment/
│   ├── Payment.java                 # Abstract Payment Processor
│   │   ├── CardPayment.java
│   │   └── CashPayment.java
│   └── PaymentStatus.java           # PENDING, COMPLETED, FAILED
└── strategy/
    ├── SpotAllocationStrategy.java  # Interface for assigning spots
    │   └── NearestSpotAllocationStrategy.java
    ├── FeeCalculationStrategy.java   # Interface for pricing
    └── FlatHourlyFeeStrategy.java
```

---

## 🌟 Core OOP Principles Applied

### 1. Abstraction
- Defined base classes like `Vehicle`, `ParkingSpot`, `Gate`, and `Payment` to hide implementation details and focus on the essential behaviors expected of these entities.
- Users of the system (like `Main.java` or `EntranceGate`) interact with these abstractions rather than concrete types, making the code decoupled.

### 2. Encapsulation
- All class fields are private/protected and exposed only via controlled getters, setters, or domain-specific mutation methods (like `park()` and `unpark()`).
- Spot counts and display board states are updated safely within `ParkingFloor` to prevent external classes from leaving the system in an inconsistent state.

### 3. Inheritance
- Reused code by setting up logical hierarchical relationships:
  - `Car`, `Motorcycle`, and `Truck` extend `Vehicle`.
  - `CompactSpot`, `MotorcycleSpot`, and `LargeSpot` extend `ParkingSpot`.
  - `CardPayment` and `CashPayment` extend `Payment`.

### 4. Polymorphism
- **Dynamic Method Binding**: The `canFit(Vehicle)` method is declared in `ParkingSpot` and overridden differently in subclass spot types:
  - `MotorcycleSpot` only fits `MOTORCYCLE`.
  - `CompactSpot` fits `CAR` or `MOTORCYCLE`.
  - `LargeSpot` fits any vehicle.
- **Strategy Selection**: The allocation and fee calculations execute dynamically based on the strategy object injected at runtime.

---

## 🎨 Design Patterns Implemented

### 🚀 Singleton Pattern
- `ParkingLot` represents a single physical entity and is implemented as a thread-safe Singleton using double-checked locking/synchronization. This ensures a single source of truth for parking spots, ticket registries, and financials.

### ⚙️ Strategy Pattern
- **Spot Allocation**: Abstracted into `SpotAllocationStrategy`. Allows the parking lot to switch algorithms (e.g., from `NearestSpotAllocationStrategy` to floor-based or random strategies) without altering the entrance gate logic.
- **Fee Calculation**: Abstracted into `FeeCalculationStrategy`. Concrete classes (e.g., `FlatHourlyFeeStrategy`) implement custom pricing algorithms. Rates can vary by vehicle type and are calculated on checkout.

### 👁️ Observer Pattern (Implicit)
- The `DisplayBoard` displays real-time available counts. When a spot's occupancy status is modified (vehicle parked or unparked), the containing `ParkingFloor` automatically recalculates availability and notifies the floor's `DisplayBoard` to update its state.

---

## 🔧 Installation & Build Instructions

This project uses **Maven** for compiling, building, and running tests.

### Prerequisites
- Java JDK 17 or higher
- Apache Maven installed

### Compilation
Build the project using Maven:
```bash
mvn clean compile
```

### Running Unit Tests
Run the JUnit 5 test suite to verify code correctness:
```bash
mvn test
```

### Starting the Interactive Simulator
Run the application using the Maven Exec plugin:
```bash
mvn exec:java -Dexec.mainClass="com.parkinglot.Main"
```

---

## 🎮 Simulation Walkthrough

When you run the simulator, you will see a console menu with options to:
1. **Park a Vehicle**: Enter a license plate and select vehicle type. The system automatically searches for the nearest compatible spot, parks the vehicle, generates a unique ticket ID, and decrements the display board.
2. **Unpark a Vehicle**: Search by ticket ID or license plate. Input the duration of parking (to simulate hours elapsed), select cash or card payment, and watch the system compute rates (rounding up to the nearest hour), receive funds, and free up the spot.
3. **View Parking Lot Map**: A visual ASCII layout of the floors showing every spot configuration (`Compact`, `Motorcycle`, `Large`) and its current occupancy status.
4. **View Display Boards**: Shows real-time available spot counts for each category on each floor.
5. **View Admin Report & Ledgers**: Tracks total tickets issued, current occupancy levels, overall revenue, and displays a complete transaction history ledger.

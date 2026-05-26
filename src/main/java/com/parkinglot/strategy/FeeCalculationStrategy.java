package com.parkinglot.strategy;

import com.parkinglot.model.VehicleType;

public interface FeeCalculationStrategy {
    double calculateFee(double durationInHours, VehicleType vehicleType);
}

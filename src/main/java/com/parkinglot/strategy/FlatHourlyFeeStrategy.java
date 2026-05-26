package com.parkinglot.strategy;

import com.parkinglot.model.VehicleType;
import java.util.HashMap;
import java.util.Map;

public class FlatHourlyFeeStrategy implements FeeCalculationStrategy {
    private final Map<VehicleType, Double> hourlyRates;

    public FlatHourlyFeeStrategy() {
        hourlyRates = new HashMap<>();
        hourlyRates.put(VehicleType.MOTORCYCLE, 2.0);
        hourlyRates.put(VehicleType.CAR, 4.0);
        hourlyRates.put(VehicleType.TRUCK, 10.0);
    }

    @Override
    public double calculateFee(double durationInHours, VehicleType vehicleType) {
        // Standard parking logic: round up to next full hour (minimum 1 hour charge)
        int hoursToCharge = (int) Math.ceil(durationInHours);
        if (hoursToCharge <= 0) {
            hoursToCharge = 1;
        }
        double rate = hourlyRates.getOrDefault(vehicleType, 4.0);
        return hoursToCharge * rate;
    }
}

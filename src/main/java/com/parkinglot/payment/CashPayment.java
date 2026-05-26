package com.parkinglot.payment;

import com.parkinglot.exception.ParkingLotException;

public class CashPayment extends Payment {
    private final double amountTendered;
    private double changeReturned;

    public CashPayment(double amount, double amountTendered) {
        super(amount);
        this.amountTendered = amountTendered;
        if (amountTendered < amount) {
            throw new ParkingLotException("Insufficient cash tendered. Required: $" + amount + ", Tendered: $" + amountTendered);
        }
    }

    public double getChangeReturned() {
        return changeReturned;
    }

    @Override
    public boolean process() {
        System.out.println("Processing cash payment of $" + getAmount() + ". Cash tendered: $" + amountTendered);
        this.changeReturned = amountTendered - getAmount();
        setStatus(PaymentStatus.COMPLETED);
        System.out.printf("Payment Success! Transaction ID: %s. Change returned: $%.2f\n", getTxnId(), changeReturned);
        return true;
    }
}

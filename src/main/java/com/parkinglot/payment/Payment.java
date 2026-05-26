package com.parkinglot.payment;

import java.util.UUID;

public abstract class Payment {
    private final String txnId;
    private final double amount;
    private PaymentStatus status;
    private final long timestamp;

    protected Payment(double amount) {
        this.txnId = "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.amount = amount;
        this.status = PaymentStatus.PENDING;
        this.timestamp = System.currentTimeMillis();
    }

    public String getTxnId() {
        return txnId;
    }

    public double getAmount() {
        return amount;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    protected void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public abstract boolean process();
}

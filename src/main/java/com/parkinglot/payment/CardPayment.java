package com.parkinglot.payment;

public class CardPayment extends Payment {
    private final String cardNumber;
    private final String cardHolderName;

    public CardPayment(double amount, String cardNumber, String cardHolderName) {
        super(amount);
        this.cardNumber = cardNumber;
        this.cardHolderName = cardHolderName;
    }

    @Override
    public boolean process() {
        System.out.println("Processing card payment of $" + getAmount() + " for card: " + maskCardNumber(cardNumber));
        // Simulate authorization delay / success
        try {
            Thread.sleep(200); // 200 ms simulation
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        setStatus(PaymentStatus.COMPLETED);
        System.out.println("Payment Success! Transaction ID: " + getTxnId());
        return true;
    }

    private String maskCardNumber(String number) {
        if (number == null || number.length() < 4) {
            return "****";
        }
        return "****-****-****-" + number.substring(number.length() - 4);
    }
}

package com.revpay.revpay_backend.dto;

public class WithdrawRequest {

    private Double amount;
    private String pin;

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getPin() { return pin; }
    public void setPin(String pin) { this.pin = pin; }
}
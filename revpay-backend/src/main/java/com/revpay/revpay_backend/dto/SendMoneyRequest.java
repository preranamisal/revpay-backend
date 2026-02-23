package com.revpay.revpay_backend.dto;

public class SendMoneyRequest {

    private Long receiverId;
    private Double amount;
    private String note;
    private String pin;

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
    
    public String getPin() {
        return pin;
    }

    
    public void setPin(String pin) {
        this.pin = pin;
    }
}
package com.revpay.revpay_backend.dto;

public class CreateMoneyRequestDTO {

    private Long payerId;
    private Double amount;
    private String note;

    public Long getPayerId() { return payerId; }
    public void setPayerId(Long payerId) { this.payerId = payerId; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}
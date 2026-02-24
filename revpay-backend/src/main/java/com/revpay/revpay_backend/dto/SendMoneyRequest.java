package com.revpay.revpay_backend.dto;

public class SendMoneyRequest {

	private String receiverIdentifier; // ID OR email OR phone
    private Double amount;
    private String note;
    private String pin;

    

    public String getReceiverIdentifier() {
		return receiverIdentifier;
	}

	public void setReceiverIdentifier(String receiverIdentifier) {
		this.receiverIdentifier = receiverIdentifier;
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
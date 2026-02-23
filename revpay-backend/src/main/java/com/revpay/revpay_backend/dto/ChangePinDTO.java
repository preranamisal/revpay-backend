package com.revpay.revpay_backend.dto;

public class ChangePinDTO {

    private String currentPin;
    private String newPin;

    public String getCurrentPin() { return currentPin; }
    public void setCurrentPin(String currentPin) { this.currentPin = currentPin; }

    public String getNewPin() { return newPin; }
    public void setNewPin(String newPin) { this.newPin = newPin; }
}
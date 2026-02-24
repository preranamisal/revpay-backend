//package com.revpay.revpay_backend.dto;
//
//public class PaymentMethodDTO {
//
//    private String cardNumber;
//    private String expiry;
//    private String cardHolderName;
//
//    public String getCardNumber() { return cardNumber; }
//    public void setCardNumber(String cardNumber) { this.cardNumber = cardNumber; }
//
//    public String getExpiry() { return expiry; }
//    public void setExpiry(String expiry) { this.expiry = expiry; }
//
//    public String getCardHolderName() { return cardHolderName; }
//    public void setCardHolderName(String cardHolderName) { this.cardHolderName = cardHolderName; }
//}

package com.revpay.revpay_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class PaymentMethodDTO {

    @NotBlank(message = "Card holder name is required")
    private String cardHolderName;

    @NotBlank(message = "Card number is required")
    @Size(min = 13, max = 19, message = "Invalid card number")
    private String cardNumber;

    @NotBlank(message = "Expiry date is required")
    @Pattern(regexp = "^(0[1-9]|1[0-2])/\\d{2}$",
             message = "Expiry must be in MM/YY format")
    private String expiry;

    @NotBlank(message = "CVV is required")
    @Size(min = 3, max = 4, message = "Invalid CVV")
    private String cvv;

    @NotBlank(message = "Billing address is required")
    private String billingAddress;

    // Getters & Setters

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
    }
}
package com.revpay.revpay_backend.dto;

public class CreatePaymentRequestDTO {

    private String customerIdentifier;
    private Double amount;
    private String description;
	public String getCustomerIdentifier() {
		return customerIdentifier;
	}
	public void setCustomerIdentifier(String customerIdentifier) {
		this.customerIdentifier = customerIdentifier;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

    // getters & setters
}
package com.revpay.revpay_backend.dto;

import jakarta.validation.constraints.NotBlank;

public class BusinessRegisterDTO {

    @NotBlank
    private String businessName;

    @NotBlank
    private String businessType;

    @NotBlank
    private String taxId;

    @NotBlank
    private String businessAddress;

    @NotBlank
    private String contactNumber;

	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public String getTaxId() {
		return taxId;
	}

	public void setTaxId(String taxId) {
		this.taxId = taxId;
	}

	public String getBusinessAddress() {
		return businessAddress;
	}

	public void setBusinessAddress(String businessAddress) {
		this.businessAddress = businessAddress;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

    // Getters & Setters
    
}
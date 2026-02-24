package com.revpay.revpay_backend.dto;

public class LoanApplicationDTO {

    private Double amount;
    private String purpose;
    private Integer tenureMonths;
    private String financialInfo;
    private String documentPath;
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public String getPurpose() {
		return purpose;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	public Integer getTenureMonths() {
		return tenureMonths;
	}
	public void setTenureMonths(Integer tenureMonths) {
		this.tenureMonths = tenureMonths;
	}
	public String getFinancialInfo() {
		return financialInfo;
	}
	public void setFinancialInfo(String financialInfo) {
		this.financialInfo = financialInfo;
	}
	public String getDocumentPath() {
		return documentPath;
	}
	public void setDocumentPath(String documentPath) {
		this.documentPath = documentPath;
	}

    // Getters & Setters
    
}
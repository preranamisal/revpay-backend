package com.revpay.revpay_backend.dto;

import java.time.LocalDate;
import java.util.List;

public class CreateInvoiceDTO {

    private String customerName;
    private List<InvoiceItemDTO> items;
    private String customerEmail;
    private String customerAddress;

    private String paymentTerms;
    private LocalDate dueDate;
    
    
	public String getCustomerEmail() {
		return customerEmail;
	}
	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}
	public String getCustomerAddress() {
		return customerAddress;
	}
	public void setCustomerAddress(String customerAddress) {
		this.customerAddress = customerAddress;
	}
	public String getPaymentTerms() {
		return paymentTerms;
	}
	public void setPaymentTerms(String paymentTerms) {
		this.paymentTerms = paymentTerms;
	}
	public LocalDate getDueDate() {
		return dueDate;
	}
	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public List<InvoiceItemDTO> getItems() {
		return items;
	}
	public void setItems(List<InvoiceItemDTO> items) {
		this.items = items;
	}

    // getters & setters
    
}
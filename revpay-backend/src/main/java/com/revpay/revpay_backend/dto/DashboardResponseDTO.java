package com.revpay.revpay_backend.dto;


import java.util.Map;
import java.util.List;

public class DashboardResponseDTO {

    private Double totalReceived;
    private Double totalSent;
    private Double pendingInvoicesAmount;

    private Map<String, Double> revenueReport;
    private List<String> topCustomers;

    // Getters & Setters

    public Double getTotalReceived() { return totalReceived; }
    public void setTotalReceived(Double totalReceived) { this.totalReceived = totalReceived; }

    public Double getTotalSent() { return totalSent; }
    public void setTotalSent(Double totalSent) { this.totalSent = totalSent; }

    public Double getPendingInvoicesAmount() { return pendingInvoicesAmount; }
    public void setPendingInvoicesAmount(Double pendingInvoicesAmount) {
        this.pendingInvoicesAmount = pendingInvoicesAmount;
    }

    public Map<String, Double> getRevenueReport() { return revenueReport; }
    public void setRevenueReport(Map<String, Double> revenueReport) {
        this.revenueReport = revenueReport;
    }

    public List<String> getTopCustomers() { return topCustomers; }
    public void setTopCustomers(List<String> topCustomers) {
        this.topCustomers = topCustomers;
    }
}
package com.revpay.revpay_backend.dto;

import java.util.List;

public class DashboardResponse {

    private Double walletBalance;
    private Long totalTransactions;
    private List<TransactionResponse> recentTransactions;

    public DashboardResponse(Double walletBalance,
                             Long totalTransactions,
                             List<TransactionResponse> recentTransactions) {
        this.walletBalance = walletBalance;
        this.totalTransactions = totalTransactions;
        this.recentTransactions = recentTransactions;
    }

    public Double getWalletBalance() {
        return walletBalance;
    }

    public Long getTotalTransactions() {
        return totalTransactions;
    }

    public List<TransactionResponse> getRecentTransactions() {
        return recentTransactions;
    }
}

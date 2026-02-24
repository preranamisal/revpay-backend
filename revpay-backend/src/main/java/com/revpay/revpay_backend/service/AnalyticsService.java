package com.revpay.revpay_backend.service;

import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import com.revpay.revpay_backend.dto.DashboardResponseDTO;
import com.revpay.revpay_backend.model.*;
import com.revpay.revpay_backend.repository.*;

@Service
public class AnalyticsService {

    private final InvoiceRepository invoiceRepo;
    private final TransactionRepository transactionRepo;

    public AnalyticsService(InvoiceRepository invoiceRepo,
                            TransactionRepository transactionRepo) {
        this.invoiceRepo = invoiceRepo;
        this.transactionRepo = transactionRepo;
    }

    public DashboardResponseDTO getDashboard(Long businessId) {

        DashboardResponseDTO dto = new DashboardResponseDTO();

        // Total Received
        Double totalReceived =
                transactionRepo.getTotalReceived(businessId);

        // Total Sent
        Double totalSent =
                transactionRepo.getTotalSent(businessId);

        dto.setTotalReceived(totalReceived != null ? totalReceived : 0.0);
        dto.setTotalSent(totalSent != null ? totalSent : 0.0);

        // Pending Invoice Amount
        List<Invoice> pendingInvoices =
                invoiceRepo.findByBusinessIdAndStatus(
                        businessId, InvoiceStatus.SENT);

        double pendingAmount = pendingInvoices.stream()
                .mapToDouble(Invoice::getTotalAmount)
                .sum();

        dto.setPendingInvoicesAmount(pendingAmount);

        // Revenue Report (Monthly example)
        Map<String, Double> revenueMap = new HashMap<>();

        List<Invoice> paidInvoices =
                invoiceRepo.findByBusinessIdAndStatus(
                        businessId, InvoiceStatus.PAID);

        for (Invoice inv : paidInvoices) {
            String month = inv.getCreatedAt()
                    .getMonth().toString();

            revenueMap.put(month,
                    revenueMap.getOrDefault(month, 0.0)
                            + inv.getTotalAmount());
        }

        dto.setRevenueReport(revenueMap);

        // Top Customers
        List<String> topCustomers = paidInvoices.stream()
                .collect(Collectors.groupingBy(
                        Invoice::getCustomerName,
                        Collectors.summingDouble(
                                Invoice::getTotalAmount)))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue()
                        .reversed())
                .limit(5)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        dto.setTopCustomers(topCustomers);

        return dto;
    }
}
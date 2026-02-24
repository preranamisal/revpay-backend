package com.revpay.revpay_backend.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.revpay.revpay_backend.dto.LoanApplicationDTO;
import com.revpay.revpay_backend.model.*;
import com.revpay.revpay_backend.repository.*;

@Service
public class LoanService {

    private final LoanRepository loanRepo;
    private final LoanRepaymentRepository repaymentRepo;
    private final NotificationService notificationService;

    public LoanService(LoanRepository loanRepo,
                       LoanRepaymentRepository repaymentRepo,
                       NotificationService notificationService) {
        this.loanRepo = loanRepo;
        this.repaymentRepo = repaymentRepo;
        this.notificationService = notificationService;
    }

    @Transactional
    public String applyLoan(Long businessId, LoanApplicationDTO dto) {

        Loan loan = new Loan();
        loan.setBusinessId(businessId);
        loan.setAmount(dto.getAmount());
        loan.setPurpose(dto.getPurpose());
        loan.setTenureMonths(dto.getTenureMonths());
        loan.setFinancialInfo(dto.getFinancialInfo());
        loan.setDocumentPath(dto.getDocumentPath());
        loan.setStatus(LoanStatus.PENDING);
        loan.setCreatedAt(LocalDateTime.now());

        loanRepo.save(loan);

        notificationService.createNotification(
                businessId,
                "Loan Application Submitted",
                "Your loan application is under review"
        );

        return "Loan application submitted successfully";
    }

    public String approveLoan(Long loanId) {

        Loan loan = loanRepo.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found"));

        loan.setInterestRate(10.0); // Fixed interest
        double emi = calculateEMI(
                loan.getAmount(),
                loan.getInterestRate(),
                loan.getTenureMonths()
        );

        loan.setEmiAmount(emi);
        loan.setStatus(LoanStatus.ACTIVE);

        loanRepo.save(loan);

        notificationService.createNotification(
                loan.getBusinessId(),
                "Loan Approved",
                "Your loan has been approved"
        );

        return "Loan approved";
    }

    public String rejectLoan(Long loanId) {

        Loan loan = loanRepo.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found"));

        loan.setStatus(LoanStatus.REJECTED);
        loanRepo.save(loan);

        return "Loan rejected";
    }

    public List<Loan> getBusinessLoans(Long businessId) {
        return loanRepo.findByBusinessId(businessId);
    }

    public String repayLoan(Long loanId, Double amount) {

        Loan loan = loanRepo.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found"));

        LoanRepayment repayment = new LoanRepayment();
        repayment.setLoanId(loanId);
        repayment.setAmountPaid(amount);
        repayment.setPaymentDate(LocalDate.now());

        repaymentRepo.save(repayment);

        return "Loan repayment recorded";
    }

    private double calculateEMI(double principal,
                                double annualRate,
                                int tenureMonths) {

        double monthlyRate = annualRate / 12 / 100;
        return (principal * monthlyRate *
                Math.pow(1 + monthlyRate, tenureMonths)) /
                (Math.pow(1 + monthlyRate, tenureMonths) - 1);
    }
}

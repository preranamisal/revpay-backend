package com.revpay.revpay_backend.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

import com.revpay.revpay_backend.dto.AddMoneyRequest;
import com.revpay.revpay_backend.dto.DashboardResponse;
import com.revpay.revpay_backend.dto.SendMoneyRequest;
import com.revpay.revpay_backend.dto.WithdrawRequest;
import com.revpay.revpay_backend.model.TransactionStatus;
import com.revpay.revpay_backend.model.TransactionType;
import com.revpay.revpay_backend.model.User;
import com.revpay.revpay_backend.repository.UserRepository;
import com.revpay.revpay_backend.service.TransactionService;

import jakarta.validation.Valid;

import java.util.List;
import com.revpay.revpay_backend.dto.TransactionResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private final UserRepository userRepository;

    public TransactionController(TransactionService transactionService,
                                 UserRepository userRepository) {
        this.transactionService = transactionService;
        this.userRepository = userRepository;
    }

    @PostMapping("/send")
    public String sendMoney(@RequestBody SendMoneyRequest request,
                            Authentication authentication) {

        String email = authentication.getName();

        User sender = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return transactionService.sendMoney(sender.getId(), request);
    }
    
    @PostMapping("/add-money")
    public String addMoney(@RequestBody AddMoneyRequest request,
                           Authentication authentication) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return transactionService.addMoney(user.getId(), request.getAmount());
    }
    
    @PostMapping("/withdraw")
    public String withdrawMoney(@RequestBody WithdrawRequest request,
                                Authentication authentication) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return transactionService.withdrawMoney(user.getId(), request.getAmount(),request.getPin());
    }
    
    @GetMapping("/history")
    public List<TransactionResponse> getHistory(Authentication authentication) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return transactionService.getTransactionHistory(user.getId());
    }
    
    
    @GetMapping("/filter")
    public List<TransactionResponse> filterTransactions(
            Authentication authentication,
            @RequestParam(required = false) TransactionType type,
            @RequestParam(required = false) TransactionStatus status,
            @RequestParam(required = false) Double minAmount,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate
    ) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        java.time.LocalDateTime start = null;
        java.time.LocalDateTime end = null;

        if (startDate != null) {
            start = java.time.LocalDate.parse(startDate).atStartOfDay();
        }

        if (endDate != null) {
            end = java.time.LocalDate.parse(endDate).atTime(23,59,59);
        }

        return transactionService.filterTransactions(
                user.getId(), type, status, minAmount, start, end);
    }
    
    @GetMapping("/dashboard")
    public DashboardResponse getDashboard(Authentication authentication) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return transactionService.getDashboard(user.getId());
    }
    
    @GetMapping("/search")
    public List<TransactionResponse> searchTransactions(
            @RequestParam String keyword,
            @AuthenticationPrincipal UserDetails userDetails) {

        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return transactionService.searchTransactions(user.getId(), keyword);
    }
    
    @GetMapping("/export/csv")
    public ResponseEntity<byte[]> exportCsv(
            @AuthenticationPrincipal UserDetails userDetails) {

        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String csv = transactionService.generateCsv(user.getId());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=transactions.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(csv.getBytes());
    }
    
    @GetMapping("/export/pdf")
    public ResponseEntity<byte[]> exportPdf(
            @AuthenticationPrincipal UserDetails userDetails) throws Exception {

        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        byte[] pdf = transactionService.generatePdf(user.getId());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=transactions.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
    
    @PostMapping("/wallet/add-money")
    public String addMoney(
            @Valid @RequestBody AddMoneyRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return transactionService.addMoneyFromCard(user.getId(), request);
    }
}
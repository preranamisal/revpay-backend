package com.revpay.revpay_backend.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

import com.revpay.revpay_backend.dto.LoanApplicationDTO;
import com.revpay.revpay_backend.model.*;
import com.revpay.revpay_backend.repository.UserRepository;
import com.revpay.revpay_backend.service.LoanService;

@RestController
@RequestMapping("/business/loan")
public class LoanController {

    private final LoanService service;
    private final UserRepository userRepository;

    public LoanController(LoanService service,
                          UserRepository userRepository) {
        this.service = service;
        this.userRepository = userRepository;
    }

    @PostMapping("/apply")
    public String apply(
            @RequestBody LoanApplicationDTO dto,
            @AuthenticationPrincipal UserDetails userDetails) {

        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return service.applyLoan(user.getId(), dto);
    }

    @GetMapping("/all")
    public List<Loan> getAll(
            @AuthenticationPrincipal UserDetails userDetails) {

        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return service.getBusinessLoans(user.getId());
    }

    @PostMapping("/{id}/approve")
    public String approve(@PathVariable Long id) {
        return service.approveLoan(id);
    }

    @PostMapping("/{id}/reject")
    public String reject(@PathVariable Long id) {
        return service.rejectLoan(id);
    }

    @PostMapping("/{id}/repay")
    public String repay(@PathVariable Long id,
                        @RequestParam Double amount) {
        return service.repayLoan(id, amount);
    }
}

package com.revpay.revpay_backend.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

import com.revpay.revpay_backend.dto.DashboardResponseDTO;
import com.revpay.revpay_backend.model.User;
import com.revpay.revpay_backend.repository.UserRepository;
import com.revpay.revpay_backend.service.AnalyticsService;

@RestController
@RequestMapping("/business/dashboard")
public class AnalyticsController {

    private final AnalyticsService service;
    private final UserRepository userRepository;

    public AnalyticsController(AnalyticsService service,
                               UserRepository userRepository) {
        this.service = service;
        this.userRepository = userRepository;
    }

    @GetMapping
    public DashboardResponseDTO getDashboard(
            @AuthenticationPrincipal UserDetails userDetails) {

        User user = userRepository
                .findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return service.getDashboard(user.getId());
    }
}
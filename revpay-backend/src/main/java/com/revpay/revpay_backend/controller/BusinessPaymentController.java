package com.revpay.revpay_backend.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.revpay.revpay_backend.dto.BusinessPaymentMethodDTO;
import com.revpay.revpay_backend.model.User;
import com.revpay.revpay_backend.model.UserRole;
import com.revpay.revpay_backend.repository.UserRepository;
import com.revpay.revpay_backend.service.BusinessPaymentService;

@RestController
@RequestMapping("/business/payments")
public class BusinessPaymentController {

    private final BusinessPaymentService service;
    private final UserRepository userRepository;

    public BusinessPaymentController(BusinessPaymentService service,
                                     UserRepository userRepository) {
        this.service = service;
        this.userRepository = userRepository;
    }

    @PostMapping("/add")
    public String addPaymentMethod(
            @RequestBody BusinessPaymentMethodDTO dto,
            @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null) {
            throw new RuntimeException("Unauthorized");
        }

        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRole() != UserRole.ROLE_BUSINESS) {
            throw new RuntimeException("Only BUSINESS users allowed");
        }

        return service.addMethod(user.getId(), dto);
    }
}
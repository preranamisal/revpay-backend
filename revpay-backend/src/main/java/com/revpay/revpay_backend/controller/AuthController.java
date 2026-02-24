package com.revpay.revpay_backend.controller;

import org.springframework.web.bind.annotation.*;

import com.revpay.revpay_backend.dto.LoginRequest;
import com.revpay.revpay_backend.dto.RegisterRequest;
import com.revpay.revpay_backend.dto.AuthResponse;
import com.revpay.revpay_backend.service.AuthService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")   // ✅ path required
public class AuthController {

    private final AuthService authService;

    // ✅ Manual constructor (no Lombok)
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public String register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }
}
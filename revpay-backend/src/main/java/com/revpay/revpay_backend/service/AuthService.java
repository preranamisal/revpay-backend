package com.revpay.revpay_backend.service;

import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.revpay.revpay_backend.dto.LoginRequest;
import com.revpay.revpay_backend.dto.RegisterRequest;
import com.revpay.revpay_backend.dto.AuthResponse;
import com.revpay.revpay_backend.model.User;
import com.revpay.revpay_backend.model.UserRole;
import com.revpay.revpay_backend.repository.UserRepository;
import com.revpay.revpay_backend.security.JwtUtil;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository,
                       BCryptPasswordEncoder encoder,
                       JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.jwtUtil = jwtUtil;
    }

    public String register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        UserRole role = request.getRole().equalsIgnoreCase("BUSINESS")
                ? UserRole.ROLE_BUSINESS
                : UserRole.ROLE_PERSONAL;

        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setPassword(encoder.encode(request.getPassword()));
        user.setRole(role);
        user.setBusinessName(request.getBusinessName());
        user.setBusinessType(request.getBusinessType());
        user.setTaxId(request.getTaxId());
        user.setBusinessAddress(request.getBusinessAddress());
        user.setWalletBalance(0.0);

        userRepository.save(user);

        return "Registration Successful";
    }

    public AuthResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!encoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid Credentials");
        }

        String token = jwtUtil.generateToken(user.getEmail());

        // 🔥 Manual object creation instead of builder
        AuthResponse response = new AuthResponse();
        response.setToken(token);
        response.setEmail(user.getEmail());
        response.setRole(user.getRole().name());

        return response;
    }
}
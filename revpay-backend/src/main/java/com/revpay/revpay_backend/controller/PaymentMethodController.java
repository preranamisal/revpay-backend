package com.revpay.revpay_backend.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import com.revpay.revpay_backend.dto.PaymentMethodDTO;
import com.revpay.revpay_backend.model.User;
import com.revpay.revpay_backend.repository.UserRepository;
import com.revpay.revpay_backend.service.PaymentMethodService;

import java.util.List;

@RestController
@RequestMapping("/payment-methods")
public class PaymentMethodController {

    private final PaymentMethodService service;
    private final UserRepository userRepository;

    public PaymentMethodController(PaymentMethodService service,
                                   UserRepository userRepository) {
        this.service = service;
        this.userRepository = userRepository;
    }

    @PostMapping("/add")
    public String add(@RequestBody PaymentMethodDTO dto,
                      Authentication authentication) {

        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return service.addMethod(user.getId(), dto);
    }

    @GetMapping
    public List<?> get(Authentication authentication) {

        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return service.getMethods(user.getId());
    }

    @PutMapping("/{id}/set-default")
    public String setDefault(@PathVariable Long id,
                             Authentication authentication) {

        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return service.setDefault(id, user.getId());
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id,
                         Authentication authentication) {

        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return service.delete(id, user.getId());
    }
}
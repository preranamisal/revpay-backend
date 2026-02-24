package com.revpay.revpay_backend.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

import com.revpay.revpay_backend.dto.CreateMoneyRequestDTO;
import com.revpay.revpay_backend.model.User;
import com.revpay.revpay_backend.repository.UserRepository;
import com.revpay.revpay_backend.service.MoneyRequestService;

import java.util.List;

@RestController
@RequestMapping("/requests")
public class MoneyRequestController {

    private final MoneyRequestService service;
    private final UserRepository userRepository;

    public MoneyRequestController(MoneyRequestService service,
                                  UserRepository userRepository) {
        this.service = service;
        this.userRepository = userRepository;
    }

    @PostMapping("/send")
    public String sendRequest(@RequestBody CreateMoneyRequestDTO dto,
                              Authentication authentication) {

        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return service.createRequest(user.getId(), dto);
    }

    @GetMapping("/incoming")
    public List<?> incoming(Authentication authentication) {

        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return service.getIncoming(user.getId());
    }

    @GetMapping("/outgoing")
    public List<?> outgoing(Authentication authentication) {

        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return service.getOutgoing(user.getId());
    }

    @PostMapping("/{id}/accept")
    public String accept(@PathVariable Long id,
                         Authentication authentication) {

        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return service.acceptRequest(id, user.getId());
    }

    @PostMapping("/{id}/decline")
    public String decline(@PathVariable Long id,
                          Authentication authentication) {

        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return service.declineRequest(id, user.getId());
    }
    
    @PostMapping("/{id}/cancel")
    public String cancelRequest(@PathVariable Long id,
                                @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null) {
            throw new RuntimeException("Unauthorized");
        }

        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return service.cancelRequest(id, user.getId());
    }
}
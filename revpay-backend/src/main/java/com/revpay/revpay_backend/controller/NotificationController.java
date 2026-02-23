package com.revpay.revpay_backend.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.revpay.revpay_backend.model.Notification;
import com.revpay.revpay_backend.model.User;
import com.revpay.revpay_backend.repository.UserRepository;
import com.revpay.revpay_backend.service.NotificationService;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService service;
    private final UserRepository userRepository;

    public NotificationController(NotificationService service,
                                  UserRepository userRepository) {
        this.service = service;
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<Notification> getNotifications(Authentication authentication) {

        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return service.getUserNotifications(user.getId());
    }

    @GetMapping("/unread-count")
    public Long getUnreadCount(Authentication authentication) {

        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return service.getUnreadCount(user.getId());
    }

    @PutMapping("/{id}/read")
    public String markAsRead(@PathVariable Long id) {
        return service.markAsRead(id);
    }
}
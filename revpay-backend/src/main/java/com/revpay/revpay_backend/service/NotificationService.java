package com.revpay.revpay_backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.revpay.revpay_backend.model.Notification;
import com.revpay.revpay_backend.repository.NotificationRepository;

@Service
public class NotificationService {

    private final NotificationRepository repository;

    public NotificationService(NotificationRepository repository) {
        this.repository = repository;
    }

    public void createNotification(Long userId, String title, String message) {
        Notification notification = new Notification(userId, title, message);
        repository.save(notification);
    }

    public List<Notification> getUserNotifications(Long userId) {
        return repository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public String markAsRead(Long notificationId) {
        Notification notification = repository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        notification.setRead(true);
        repository.save(notification);

        return "Notification marked as read";
    }

    public Long getUnreadCount(Long userId) {
        return repository.countByUserIdAndIsReadFalse(userId);
    }
}

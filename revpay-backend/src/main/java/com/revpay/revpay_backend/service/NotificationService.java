//package com.revpay.revpay_backend.service;
//
//import java.util.List;
//
//import org.springframework.stereotype.Service;
//
//import com.revpay.revpay_backend.model.Notification;
//import com.revpay.revpay_backend.repository.NotificationRepository;
//
//@Service
//public class NotificationService {
//
//    private final NotificationRepository repository;
//
//    public NotificationService(NotificationRepository repository) {
//        this.repository = repository;
//    }
//
//    public void createNotification(Long userId, String title, String message) {
//        Notification notification = new Notification(userId, title, message);
//        repository.save(notification);
//    }
//
//    public List<Notification> getUserNotifications(Long userId) {
//        return repository.findByUserIdOrderByCreatedAtDesc(userId);
//    }
//
//    public String markAsRead(Long notificationId) {
//        Notification notification = repository.findById(notificationId)
//                .orElseThrow(() -> new RuntimeException("Notification not found"));
//
//        notification.setRead(true);
//        repository.save(notification);
//
//        return "Notification marked as read";
//    }
//
//    public Long getUnreadCount(Long userId) {
//        return repository.countByUserIdAndIsReadFalse(userId);
//    }
//}

package com.revpay.revpay_backend.service;

import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

import com.revpay.revpay_backend.model.*;
import com.revpay.revpay_backend.repository.*;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepo;
    private final NotificationPreferenceRepository preferenceRepo;
    private final EmailService emailService;

    public NotificationService(NotificationRepository notificationRepo,
                               NotificationPreferenceRepository preferenceRepo,
                               EmailService emailService) {
        this.notificationRepo = notificationRepo;
        this.preferenceRepo = preferenceRepo;
        this.emailService = emailService;
    }

    public void sendNotification(Long userId,
                                 NotificationType type,
                                 String title,
                                 String message,
                                 String email) {

        boolean enabled = preferenceRepo
                .findByUserIdAndType(userId, type)
                .map(NotificationPreference::isEnabled)
                .orElse(true);

        if (!enabled) return;

        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setType(type);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setReadStatus(false);
        notification.setCreatedAt(LocalDateTime.now());

        notificationRepo.save(notification);

        if (email != null) {
            emailService.sendEmail(email, title, message);
        }
    }

    public List<Notification> getUserNotifications(Long userId) {
        return notificationRepo.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public String markAsRead(Long id) {
        Notification notification = notificationRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        notification.setReadStatus(true);
        notificationRepo.save(notification);

        return "Notification marked as read";
    }

    public String updatePreference(Long userId,
                                   NotificationType type,
                                   boolean enabled) {

        NotificationPreference pref = preferenceRepo
                .findByUserIdAndType(userId, type)
                .orElse(new NotificationPreference());

        pref.setUserId(userId);
        pref.setType(type);
        pref.setEnabled(enabled);

        preferenceRepo.save(pref);

        return "Preference updated";
    }
    
    public void createNotification(Long userId,
            String title,
            String message) {

Notification notification = new Notification();
notification.setUserId(userId);
notification.setTitle(title);
notification.setMessage(message);
notification.setType(NotificationType.GENERAL);
notification.setReadStatus(false);
notification.setCreatedAt(LocalDateTime.now());

notificationRepo.save(notification);
}
}
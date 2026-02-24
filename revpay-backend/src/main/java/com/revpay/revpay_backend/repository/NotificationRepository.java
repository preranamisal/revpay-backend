package com.revpay.revpay_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.revpay.revpay_backend.model.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId);

    //Long countByUserIdAndIsReadFalse(Long userId);
    Long countByUserIdAndReadStatusFalse(Long userId);
}

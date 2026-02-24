package com.revpay.revpay_backend.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.revpay.revpay_backend.model.*;

public interface NotificationPreferenceRepository
        extends JpaRepository<NotificationPreference, Long> {

    Optional<NotificationPreference> findByUserIdAndType(Long userId,
                                                         NotificationType type);
}
package com.revpay.revpay_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import com.revpay.revpay_backend.model.BusinessProfile;

public interface BusinessProfileRepository 
        extends JpaRepository<BusinessProfile, Long> {

    Optional<BusinessProfile> findByUserId(Long userId);
}
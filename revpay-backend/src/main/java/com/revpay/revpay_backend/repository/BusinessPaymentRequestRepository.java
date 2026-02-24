package com.revpay.revpay_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.revpay.revpay_backend.model.BusinessPaymentRequest;

public interface BusinessPaymentRequestRepository
        extends JpaRepository<BusinessPaymentRequest, Long> {
}
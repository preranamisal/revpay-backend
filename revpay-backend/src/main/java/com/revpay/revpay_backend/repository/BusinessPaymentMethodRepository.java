package com.revpay.revpay_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import com.revpay.revpay_backend.model.BusinessPaymentMethod;

public interface BusinessPaymentMethodRepository 
        extends JpaRepository<BusinessPaymentMethod, Long> {

    List<BusinessPaymentMethod> findByBusinessUserId(Long userId);
}
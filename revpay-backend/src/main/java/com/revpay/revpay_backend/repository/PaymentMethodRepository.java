package com.revpay.revpay_backend.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.revpay.revpay_backend.model.PaymentMethod;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {

    List<PaymentMethod> findByUserId(Long userId);
}
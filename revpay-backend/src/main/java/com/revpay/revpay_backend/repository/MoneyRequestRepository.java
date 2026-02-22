package com.revpay.revpay_backend.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.revpay.revpay_backend.model.MoneyRequest;

public interface MoneyRequestRepository extends JpaRepository<MoneyRequest, Long> {

    List<MoneyRequest> findByPayerIdOrderByCreatedAtDesc(Long payerId);

    List<MoneyRequest> findByRequesterIdOrderByCreatedAtDesc(Long requesterId);

}
package com.revpay.revpay_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.revpay.revpay_backend.model.Transaction;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findBySenderIdOrReceiverId(Long senderId, Long receiverId);
}
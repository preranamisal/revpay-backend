package com.revpay.revpay_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.revpay.revpay_backend.model.Transaction;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findBySenderIdOrReceiverId(Long senderId, Long receiverId);
    
    List<Transaction> findBySenderIdOrReceiverIdOrderByCreatedAtDesc(Long senderId, Long receiverId);
    
    @Query("""
            SELECT t FROM Transaction t
            WHERE (t.senderId = :userId OR t.receiverId = :userId)
            AND (:type IS NULL OR t.type = :type)
            AND (:status IS NULL OR t.status = :status)
            AND (:minAmount IS NULL OR t.amount >= :minAmount)
            AND (:startDate IS NULL OR t.createdAt >= :startDate)
            AND (:endDate IS NULL OR t.createdAt <= :endDate)
            ORDER BY t.createdAt DESC
        """)
        List<Transaction> filterTransactions(
                Long userId,
                com.revpay.revpay_backend.model.TransactionType type,
                com.revpay.revpay_backend.model.TransactionStatus status,
                Double minAmount,
                LocalDateTime startDate,
                LocalDateTime endDate
        );
    
    List<Transaction> findTop5BySenderIdOrReceiverIdOrderByCreatedAtDesc(
            Long senderId, Long receiverId);

}
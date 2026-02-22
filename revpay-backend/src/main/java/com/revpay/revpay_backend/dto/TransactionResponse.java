package com.revpay.revpay_backend.dto;

import java.time.LocalDateTime;

import com.revpay.revpay_backend.model.TransactionStatus;
import com.revpay.revpay_backend.model.TransactionType;

public class TransactionResponse {

    private Long id;
    private Long senderId;
    private Long receiverId;
    private Double amount;
    private TransactionType type;
    private TransactionStatus status;
    private String note;
    private LocalDateTime createdAt;

    public TransactionResponse(Long id, Long senderId, Long receiverId,
                               Double amount, TransactionType type,
                               TransactionStatus status,
                               String note, LocalDateTime createdAt) {
        this.id = id;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.amount = amount;
        this.type = type;
        this.status = status;
        this.note = note;
        this.createdAt = createdAt;
    }

    // Getters only
    public Long getId() { return id; }
    public Long getSenderId() { return senderId; }
    public Long getReceiverId() { return receiverId; }
    public Double getAmount() { return amount; }
    public TransactionType getType() { return type; }
    public TransactionStatus getStatus() { return status; }
    public String getNote() { return note; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
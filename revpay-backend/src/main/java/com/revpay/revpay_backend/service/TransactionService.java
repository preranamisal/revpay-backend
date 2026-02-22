

package com.revpay.revpay_backend.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.revpay.revpay_backend.dto.SendMoneyRequest;
import com.revpay.revpay_backend.model.*;
import com.revpay.revpay_backend.repository.TransactionRepository;
import com.revpay.revpay_backend.repository.UserRepository;

@Service
public class TransactionService {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    public TransactionService(UserRepository userRepository,
                              TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

    // ==========================
    // SEND MONEY
    // ==========================
    @Transactional
    public String sendMoney(Long senderId, SendMoneyRequest request) {

        if (request.getAmount() == null || request.getAmount() <= 0) {
            throw new RuntimeException("Invalid amount");
        }

        if (request.getReceiverId() == null) {
            throw new RuntimeException("Receiver ID required");
        }

        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        User receiver = userRepository.findById(request.getReceiverId())
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        if (sender.getWalletBalance() < request.getAmount()) {
            throw new RuntimeException("Insufficient balance");
        }

        // Deduct from sender
        sender.setWalletBalance(sender.getWalletBalance() - request.getAmount());

        // Add to receiver
        receiver.setWalletBalance(receiver.getWalletBalance() + request.getAmount());

        userRepository.save(sender);
        userRepository.save(receiver);

        // Create transaction
        Transaction transaction = new Transaction();
        transaction.setSenderId(sender.getId());
        transaction.setReceiverId(receiver.getId());
        transaction.setAmount(request.getAmount());
        transaction.setType(TransactionType.SEND);
        transaction.setStatus(TransactionStatus.SUCCESS);
        transaction.setNote(request.getNote());
        transaction.setCreatedAt(LocalDateTime.now());

        transactionRepository.save(transaction);

        return "Money sent successfully";
    }

    // ==========================
    // ADD MONEY
    // ==========================
    @Transactional
    public String addMoney(Long userId, Double amount) {

        if (amount == null || amount <= 0) {
            throw new RuntimeException("Invalid amount");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Add balance
        user.setWalletBalance(user.getWalletBalance() + amount);
        userRepository.save(user);

        // Create transaction record
        Transaction transaction = new Transaction();
        transaction.setSenderId(user.getId());
        transaction.setReceiverId(user.getId());
        transaction.setAmount(amount);
        transaction.setType(TransactionType.ADD_MONEY);
        transaction.setStatus(TransactionStatus.SUCCESS);
        transaction.setNote("Wallet top-up");
        transaction.setCreatedAt(LocalDateTime.now());

        transactionRepository.save(transaction);

        return "Money added successfully";
    }
    
    
    
    @Transactional
    public String withdrawMoney(Long userId, Double amount) {

        if (amount == null || amount <= 0) {
            throw new RuntimeException("Invalid amount");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getWalletBalance() < amount) {
            throw new RuntimeException("Insufficient balance");
        }

        // Deduct balance
        user.setWalletBalance(user.getWalletBalance() - amount);
        userRepository.save(user);

        // Create transaction record
        Transaction transaction = new Transaction();
        transaction.setSenderId(user.getId());
        transaction.setReceiverId(user.getId());
        transaction.setAmount(amount);
        transaction.setType(TransactionType.WITHDRAW);
        transaction.setStatus(TransactionStatus.SUCCESS);
        transaction.setNote("Wallet withdrawal");
        transaction.setCreatedAt(java.time.LocalDateTime.now());

        transactionRepository.save(transaction);

        return "Money withdrawn successfully";
    }
}
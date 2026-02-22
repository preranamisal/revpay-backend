package com.revpay.revpay_backend.service;

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

    @Transactional
    public String sendMoney(Long senderId, SendMoneyRequest request) {

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

        transactionRepository.save(transaction);

        return "Money sent successfully";
    }
}
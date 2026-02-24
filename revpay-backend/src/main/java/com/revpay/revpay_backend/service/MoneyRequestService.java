package com.revpay.revpay_backend.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.revpay.revpay_backend.dto.CreateMoneyRequestDTO;
import com.revpay.revpay_backend.model.*;
import com.revpay.revpay_backend.repository.*;

@Service
public class MoneyRequestService {

    private final MoneyRequestRepository moneyRequestRepository;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    public MoneyRequestService(MoneyRequestRepository moneyRequestRepository,
                               UserRepository userRepository,
                               TransactionRepository transactionRepository) {
        this.moneyRequestRepository = moneyRequestRepository;
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

    // SEND REQUEST
    public String createRequest(Long requesterId, CreateMoneyRequestDTO dto) {

        MoneyRequest request = new MoneyRequest();
        request.setRequesterId(requesterId);
        request.setPayerId(dto.getPayerId());
        request.setAmount(dto.getAmount());
        request.setNote(dto.getNote());
        request.setStatus(MoneyRequestStatus.PENDING);
        request.setCreatedAt(LocalDateTime.now());

        moneyRequestRepository.save(request);

        return "Money request sent";
    }

    // VIEW INCOMING
    public List<MoneyRequest> getIncoming(Long userId) {
        return moneyRequestRepository.findByPayerIdOrderByCreatedAtDesc(userId);
    }

    // VIEW OUTGOING
    public List<MoneyRequest> getOutgoing(Long userId) {
        return moneyRequestRepository.findByRequesterIdOrderByCreatedAtDesc(userId);
    }

    // ACCEPT REQUEST
    @Transactional
    public String acceptRequest(Long requestId, Long payerId) {

        MoneyRequest request = moneyRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if (!request.getPayerId().equals(payerId)) {
            throw new RuntimeException("Unauthorized");
        }

        if (request.getStatus() != MoneyRequestStatus.PENDING) {
            throw new RuntimeException("Already processed");
        }

        User payer = userRepository.findById(payerId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        User requester = userRepository.findById(request.getRequesterId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (payer.getWalletBalance() < request.getAmount()) {
            throw new RuntimeException("Insufficient balance");
        }

        // Transfer money
        payer.setWalletBalance(payer.getWalletBalance() - request.getAmount());
        requester.setWalletBalance(requester.getWalletBalance() + request.getAmount());

        userRepository.save(payer);
        userRepository.save(requester);

        // Create transaction record
        Transaction tx = new Transaction();
        tx.setSenderId(payer.getId());
        tx.setReceiverId(requester.getId());
        tx.setAmount(request.getAmount());
        tx.setType(TransactionType.SEND);
        tx.setStatus(TransactionStatus.SUCCESS);
        tx.setNote("Money request accepted");
        tx.setCreatedAt(LocalDateTime.now());

        transactionRepository.save(tx);

        request.setStatus(MoneyRequestStatus.ACCEPTED);
        moneyRequestRepository.save(request);

        return "Request accepted and money transferred";
    }

    // DECLINE
    public String declineRequest(Long requestId, Long payerId) {

        MoneyRequest request = moneyRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if (!request.getPayerId().equals(payerId)) {
            throw new RuntimeException("Unauthorized");
        }

        request.setStatus(MoneyRequestStatus.DECLINED);
        moneyRequestRepository.save(request);

        return "Request declined";
    }
    
    @Transactional
    public String cancelRequest(Long requestId, Long userId) {

        MoneyRequest request = moneyRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        // Only requester can cancel
        if (!request.getRequesterId().equals(userId)) {
            throw new RuntimeException("Unauthorized action");
        }

        if (request.getStatus() != MoneyRequestStatus.PENDING) {
            throw new RuntimeException("Only pending requests can be cancelled");
        }

        request.setStatus(MoneyRequestStatus.CANCELLED);
        moneyRequestRepository.save(request);

        return "Request cancelled successfully";
    }
}
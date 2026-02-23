//
//
//package com.revpay.revpay_backend.service;
//
//import java.time.LocalDateTime;
//
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import com.revpay.revpay_backend.dto.DashboardResponse;
//import com.revpay.revpay_backend.dto.SendMoneyRequest;
//import com.revpay.revpay_backend.model.*;
//import com.revpay.revpay_backend.repository.TransactionRepository;
//import com.revpay.revpay_backend.repository.UserRepository;
//
//
//import java.util.List;
//import java.util.stream.Collectors;
//import com.revpay.revpay_backend.dto.TransactionResponse;
//@Service
//public class TransactionService {
//
//    private final UserRepository userRepository;
//    private final TransactionRepository transactionRepository;
//
//    public TransactionService(UserRepository userRepository,
//                              TransactionRepository transactionRepository) {
//        this.userRepository = userRepository;
//        this.transactionRepository = transactionRepository;
//    }
//
//    // ==========================
//    // SEND MONEY
//    // ==========================
//    @Transactional
//    public String sendMoney(Long senderId, SendMoneyRequest request) {
//
//        if (request.getAmount() == null || request.getAmount() <= 0) {
//            throw new RuntimeException("Invalid amount");
//        }
//
//        if (request.getReceiverId() == null) {
//            throw new RuntimeException("Receiver ID required");
//        }
//
//        User sender = userRepository.findById(senderId)
//                .orElseThrow(() -> new RuntimeException("Sender not found"));
//
//        User receiver = userRepository.findById(request.getReceiverId())
//                .orElseThrow(() -> new RuntimeException("Receiver not found"));
//
//        if (sender.getWalletBalance() < request.getAmount()) {
//            throw new RuntimeException("Insufficient balance");
//        }
//
//        // Deduct from sender
//        sender.setWalletBalance(sender.getWalletBalance() - request.getAmount());
//
//        // Add to receiver
//        receiver.setWalletBalance(receiver.getWalletBalance() + request.getAmount());
//
//        userRepository.save(sender);
//        userRepository.save(receiver);
//
//        // Create transaction
//        Transaction transaction = new Transaction();
//        transaction.setSenderId(sender.getId());
//        transaction.setReceiverId(receiver.getId());
//        transaction.setAmount(request.getAmount());
//        transaction.setType(TransactionType.SEND);
//        transaction.setStatus(TransactionStatus.SUCCESS);
//        transaction.setNote(request.getNote());
//        transaction.setCreatedAt(LocalDateTime.now());
//
//        transactionRepository.save(transaction);
//
//        return "Money sent successfully";
//    }
//
//    // ==========================
//    // ADD MONEY
//    // ==========================
//    @Transactional
//    public String addMoney(Long userId, Double amount) {
//
//        if (amount == null || amount <= 0) {
//            throw new RuntimeException("Invalid amount");
//        }
//
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        // Add balance
//        user.setWalletBalance(user.getWalletBalance() + amount);
//        userRepository.save(user);
//
//        // Create transaction record
//        Transaction transaction = new Transaction();
//        transaction.setSenderId(user.getId());
//        transaction.setReceiverId(user.getId());
//        transaction.setAmount(amount);
//        transaction.setType(TransactionType.ADD_MONEY);
//        transaction.setStatus(TransactionStatus.SUCCESS);
//        transaction.setNote("Wallet top-up");
//        transaction.setCreatedAt(LocalDateTime.now());
//
//        transactionRepository.save(transaction);
//
//        return "Money added successfully";
//    }
//    
//    
//    
//    @Transactional
//    public String withdrawMoney(Long userId, Double amount) {
//
//        if (amount == null || amount <= 0) {
//            throw new RuntimeException("Invalid amount");
//        }
//
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        if (user.getWalletBalance() < amount) {
//            throw new RuntimeException("Insufficient balance");
//        }
//
//        // Deduct balance
//        user.setWalletBalance(user.getWalletBalance() - amount);
//        userRepository.save(user);
//
//        // Create transaction record
//        Transaction transaction = new Transaction();
//        transaction.setSenderId(user.getId());
//        transaction.setReceiverId(user.getId());
//        transaction.setAmount(amount);
//        transaction.setType(TransactionType.WITHDRAW);
//        transaction.setStatus(TransactionStatus.SUCCESS);
//        transaction.setNote("Wallet withdrawal");
//        transaction.setCreatedAt(java.time.LocalDateTime.now());
//
//        transactionRepository.save(transaction);
//
//        return "Money withdrawn successfully";
//    }
//    
//    
//    @Transactional(readOnly = true)
//    public List<TransactionResponse> getTransactionHistory(Long userId) {
//
//        List<Transaction> transactions =
//                transactionRepository
//                    .findBySenderIdOrReceiverIdOrderByCreatedAtDesc(userId, userId);
//
//        return transactions.stream()
//                .map(tx -> new TransactionResponse(
//                        tx.getId(),
//                        tx.getSenderId(),
//                        tx.getReceiverId(),
//                        tx.getAmount(),
//                        tx.getType(),
//                        tx.getStatus(),
//                        tx.getNote(),
//                        tx.getCreatedAt()
//                ))
//                .toList();
//    }
//    
//    @Transactional(readOnly = true)
//    public List<TransactionResponse> filterTransactions(
//            Long userId,
//            TransactionType type,
//            TransactionStatus status,
//            Double minAmount,
//            LocalDateTime startDate,
//            LocalDateTime endDate) {
//
//        List<Transaction> transactions =
//                transactionRepository.filterTransactions(
//                        userId, type, status, minAmount, startDate, endDate);
//
//        return transactions.stream()
//                .map(tx -> new TransactionResponse(
//                        tx.getId(),
//                        tx.getSenderId(),
//                        tx.getReceiverId(),
//                        tx.getAmount(),
//                        tx.getType(),
//                        tx.getStatus(),
//                        tx.getNote(),
//                        tx.getCreatedAt()
//                ))
//                .toList();
//    }
//    
//    @Transactional(readOnly = true)
//    public DashboardResponse getDashboard(Long userId) {
//
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        List<Transaction> recent =
//                transactionRepository
//                        .findTop5BySenderIdOrReceiverIdOrderByCreatedAtDesc(userId, userId);
//
//        Long total =
//                transactionRepository
//                        .findBySenderIdOrReceiverIdOrderByCreatedAtDesc(userId, userId)
//                        .stream()
//                        .count();
//
//        List<TransactionResponse> responseList =
//                recent.stream()
//                        .map(tx -> new TransactionResponse(
//                                tx.getId(),
//                                tx.getSenderId(),
//                                tx.getReceiverId(),
//                                tx.getAmount(),
//                                tx.getType(),
//                                tx.getStatus(),
//                                tx.getNote(),
//                                tx.getCreatedAt()
//                        ))
//                        .toList();
//
//        return new DashboardResponse(
//                user.getWalletBalance(),
//                total,
//                responseList
//        );
//    }
//}


package com.revpay.revpay_backend.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.revpay.revpay_backend.dto.DashboardResponse;
import com.revpay.revpay_backend.dto.SendMoneyRequest;
import com.revpay.revpay_backend.dto.TransactionResponse;
import com.revpay.revpay_backend.model.*;
import com.revpay.revpay_backend.repository.TransactionRepository;
import com.revpay.revpay_backend.repository.UserRepository;

@Service
public class TransactionService {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final PasswordEncoder passwordEncoder;

    public TransactionService(UserRepository userRepository,
                              TransactionRepository transactionRepository,
                              PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ==========================
    // SEND MONEY (WITH PIN)
    // ==========================
    @Transactional
    public String sendMoney(Long senderId, SendMoneyRequest request) {

        if (request.getAmount() == null || request.getAmount() <= 0) {
            throw new RuntimeException("Invalid amount");
        }

        if (request.getReceiverId() == null) {
            throw new RuntimeException("Receiver ID required");
        }

        if (request.getPin() == null) {
            throw new RuntimeException("Transaction PIN required");
        }

        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        User receiver = userRepository.findById(request.getReceiverId())
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        // 🔐 PIN verification
        if (sender.getTransactionPin() == null ||
            !passwordEncoder.matches(request.getPin(), sender.getTransactionPin())) {
            throw new RuntimeException("Invalid Transaction PIN");
        }

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

        user.setWalletBalance(user.getWalletBalance() + amount);
        userRepository.save(user);

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

    // ==========================
    // WITHDRAW (WITH PIN)
    // ==========================
    @Transactional
    public String withdrawMoney(Long userId, Double amount, String pin) {

        if (amount == null || amount <= 0) {
            throw new RuntimeException("Invalid amount");
        }

        if (pin == null) {
            throw new RuntimeException("Transaction PIN required");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 🔐 PIN verification
        if (user.getTransactionPin() == null ||
            !passwordEncoder.matches(pin, user.getTransactionPin())) {
            throw new RuntimeException("Invalid Transaction PIN");
        }

        if (user.getWalletBalance() < amount) {
            throw new RuntimeException("Insufficient balance");
        }

        user.setWalletBalance(user.getWalletBalance() - amount);
        userRepository.save(user);

        Transaction transaction = new Transaction();
        transaction.setSenderId(user.getId());
        transaction.setReceiverId(user.getId());
        transaction.setAmount(amount);
        transaction.setType(TransactionType.WITHDRAW);
        transaction.setStatus(TransactionStatus.SUCCESS);
        transaction.setNote("Wallet withdrawal");
        transaction.setCreatedAt(LocalDateTime.now());

        transactionRepository.save(transaction);

        return "Money withdrawn successfully";
    }

    // ==========================
    // TRANSACTION HISTORY
    // ==========================
    @Transactional(readOnly = true)
    public List<TransactionResponse> getTransactionHistory(Long userId) {

        return transactionRepository
                .findBySenderIdOrReceiverIdOrderByCreatedAtDesc(userId, userId)
                .stream()
                .map(tx -> new TransactionResponse(
                        tx.getId(),
                        tx.getSenderId(),
                        tx.getReceiverId(),
                        tx.getAmount(),
                        tx.getType(),
                        tx.getStatus(),
                        tx.getNote(),
                        tx.getCreatedAt()
                ))
                .toList();
    }

    // ==========================
    // DASHBOARD
    // ==========================
    @Transactional(readOnly = true)
    public DashboardResponse getDashboard(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<TransactionResponse> recent =
                transactionRepository
                        .findTop5BySenderIdOrReceiverIdOrderByCreatedAtDesc(userId, userId)
                        .stream()
                        .map(tx -> new TransactionResponse(
                                tx.getId(),
                                tx.getSenderId(),
                                tx.getReceiverId(),
                                tx.getAmount(),
                                tx.getType(),
                                tx.getStatus(),
                                tx.getNote(),
                                tx.getCreatedAt()
                        ))
                        .toList();

        Long total =
                transactionRepository
                        .findBySenderIdOrReceiverIdOrderByCreatedAtDesc(userId, userId)
                        .stream()
                        .count();

        return new DashboardResponse(
                user.getWalletBalance(),
                total,
                recent
        );
    }
    
    @Transactional(readOnly = true)
    public List<TransactionResponse> filterTransactions(
            Long userId,
            TransactionType type,
            TransactionStatus status,
            Double minAmount,
            LocalDateTime startDate,
            LocalDateTime endDate) {

        List<Transaction> transactions =
                transactionRepository.filterTransactions(
                        userId, type, status, minAmount, startDate, endDate);

        return transactions.stream()
                .map(tx -> new TransactionResponse(
                        tx.getId(),
                        tx.getSenderId(),
                        tx.getReceiverId(),
                        tx.getAmount(),
                        tx.getType(),
                        tx.getStatus(),
                        tx.getNote(),
                        tx.getCreatedAt()
                ))
                .toList();
    }
}
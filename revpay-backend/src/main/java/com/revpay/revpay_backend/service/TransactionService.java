


package com.revpay.revpay_backend.service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.revpay.revpay_backend.dto.AddMoneyRequest;
import com.revpay.revpay_backend.dto.DashboardResponse;
import com.revpay.revpay_backend.dto.SendMoneyRequest;
import com.revpay.revpay_backend.dto.TransactionResponse;
import com.revpay.revpay_backend.model.*;
import com.revpay.revpay_backend.repository.PaymentMethodRepository;
import com.revpay.revpay_backend.repository.TransactionRepository;
import com.revpay.revpay_backend.repository.UserRepository;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;

@Service
public class TransactionService {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final PasswordEncoder passwordEncoder;
    private final NotificationService notificationService;
    private final PaymentMethodRepository paymentMethodRepository;
    
    public TransactionService(UserRepository userRepository,
            TransactionRepository transactionRepository,
            PasswordEncoder passwordEncoder,
            NotificationService notificationService,
            PaymentMethodRepository paymentMethodRepository) {
this.userRepository = userRepository;
this.transactionRepository = transactionRepository;
this.passwordEncoder = passwordEncoder;
this.notificationService = notificationService;
this.paymentMethodRepository=paymentMethodRepository;
}

    // ==========================
    // SEND MONEY (WITH PIN)
    // ==========================
    @Transactional
    public String sendMoney(Long senderId, SendMoneyRequest request) {

        if (request.getAmount() == null || request.getAmount() <= 0) {
            throw new RuntimeException("Invalid amount");
        }

        if (request.getPin() == null || request.getPin().isBlank()) {
            throw new RuntimeException("Transaction PIN required");
        }

        if (request.getReceiverIdentifier() == null || request.getReceiverIdentifier().isBlank()) {
            throw new RuntimeException("Receiver identifier required");
        }

        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        // 🔎 Identify receiver (by ID OR email OR phone)
        String identifier = request.getReceiverIdentifier();
        User receiver;

        // Try ID first
        try {
            Long receiverId = Long.parseLong(identifier);
            receiver = userRepository.findById(receiverId).orElse(null);
        } catch (NumberFormatException e) {
            receiver = null;
        }

        // If not found by ID, try email or phone
        if (receiver == null) {
            receiver = userRepository.findByEmail(identifier)
                    .or(() -> userRepository.findByPhone(identifier))
                    .orElseThrow(() -> new RuntimeException("Receiver not found"));
        }

        // 🔐 PIN verification
        if (sender.getTransactionPin() == null ||
                !passwordEncoder.matches(request.getPin(), sender.getTransactionPin())) {
            throw new RuntimeException("Invalid Transaction PIN");
        }

        if (sender.getWalletBalance() < request.getAmount()) {
            throw new RuntimeException("Insufficient balance");
        }

        // 💸 Deduct from sender
        sender.setWalletBalance(sender.getWalletBalance() - request.getAmount());

        // 💰 Add to receiver
        receiver.setWalletBalance(receiver.getWalletBalance() + request.getAmount());

        userRepository.save(sender);
        userRepository.save(receiver);

        // 🧾 Create transaction record
        Transaction transaction = new Transaction();
        transaction.setSenderId(sender.getId());
        transaction.setReceiverId(receiver.getId());
        transaction.setAmount(request.getAmount());
        transaction.setType(TransactionType.SEND);
        transaction.setStatus(TransactionStatus.SUCCESS);
        transaction.setNote(request.getNote());
        transaction.setCreatedAt(LocalDateTime.now());

        transactionRepository.save(transaction);

        // 🔔 Notifications (after success)
        notificationService.createNotification(
                sender.getId(),
                "Money Sent",
                "You sent ₹" + request.getAmount() + " to " + identifier
        );

        notificationService.createNotification(
                receiver.getId(),
                "Money Received",
                "You received ₹" + request.getAmount() + " from " + sender.getEmail()
        );

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
        
        notificationService.createNotification(
                user.getId(),
                "Wallet Credited",
                "₹" + amount + " added to your wallet"
        );

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
        
        notificationService.createNotification(
                user.getId(),
                "Withdrawal Successful",
                "₹" + amount + " withdrawn from wallet"
        );

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
    
    @Transactional(readOnly = true)
    public List<TransactionResponse> searchTransactions(Long userId, String keyword) {

        List<Transaction> transactions =
                transactionRepository.searchTransactions(keyword);

        // Only return transactions related to logged-in user
        return transactions.stream()
                .filter(tx -> tx.getSenderId().equals(userId)
                           || tx.getReceiverId().equals(userId))
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
    
    @Transactional(readOnly = true)
    public String generateCsv(Long userId) {

        List<Transaction> transactions =
                transactionRepository
                        .findBySenderIdOrReceiverIdOrderByCreatedAtDesc(userId, userId);

        StringBuilder sb = new StringBuilder();

        sb.append("Transaction ID,Sender ID,Receiver ID,Amount,Type,Status,Note,Date\n");

        for (Transaction t : transactions) {
            sb.append(t.getId()).append(",")
              .append(t.getSenderId()).append(",")
              .append(t.getReceiverId()).append(",")
              .append(t.getAmount()).append(",")
              .append(t.getType()).append(",")
              .append(t.getStatus()).append(",")
              .append(t.getNote()).append(",")
              .append(t.getCreatedAt()).append("\n");
        }

        return sb.toString();
    }
    
    
    @Transactional(readOnly = true)
    public byte[] generatePdf(Long userId) throws Exception {

        List<Transaction> transactions =
                transactionRepository
                        .findBySenderIdOrReceiverIdOrderByCreatedAtDesc(userId, userId);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, out);

        document.open();
        document.add(new Paragraph("Transaction History\n\n"));

        for (Transaction t : transactions) {
            document.add(new Paragraph(
                    "ID: " + t.getId() +
                    " | Amount: " + t.getAmount() +
                    " | Type: " + t.getType() +
                    " | Status: " + t.getStatus() +
                    " | Date: " + t.getCreatedAt()
            ));
        }

        document.close();
        return out.toByteArray();
    }
    
    @Transactional
    public String addMoneyFromCard(Long userId, AddMoneyRequest request) {

        if (request.getAmount() <= 0) {
            throw new RuntimeException("Invalid amount");
        }

        // Validate card exists
        PaymentMethod card = paymentMethodRepository.findById(request.getCardId())
                .orElseThrow(() -> new RuntimeException("Card not found"));

        // Validate card belongs to user
        if (!card.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized card access");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 💰 Simulated payment success
        user.setWalletBalance(user.getWalletBalance() + request.getAmount());
        userRepository.save(user);

        // 🧾 Create transaction record
        Transaction transaction = new Transaction();
        transaction.setSenderId(userId);
        transaction.setReceiverId(userId);
        transaction.setAmount(request.getAmount());
        transaction.setType(TransactionType.ADD_MONEY);
        transaction.setStatus(TransactionStatus.SUCCESS);
        transaction.setNote("Added via card ending " +
                card.getCardNumber().substring(card.getCardNumber().length() - 4));
        transaction.setCreatedAt(LocalDateTime.now());

        transactionRepository.save(transaction);

        // 🔔 Create notification
        notificationService.createNotification(
                userId,
                "Wallet Top-up Successful",
                "₹" + request.getAmount() + " added to your wallet"
        );

        return "Money added successfully via card";
    }
}
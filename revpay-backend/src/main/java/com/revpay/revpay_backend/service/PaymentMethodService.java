package com.revpay.revpay_backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.revpay.revpay_backend.dto.PaymentMethodDTO;
import com.revpay.revpay_backend.model.PaymentMethod;
import com.revpay.revpay_backend.repository.PaymentMethodRepository;

@Service
public class PaymentMethodService {

    private final PaymentMethodRepository repository;

    public PaymentMethodService(PaymentMethodRepository repository) {
        this.repository = repository;
    }

    // Add card
    public String addMethod(Long userId, PaymentMethodDTO dto) {

        PaymentMethod method = new PaymentMethod();
        method.setUserId(userId);
        method.setCardNumber(dto.getCardNumber());
        method.setExpiry(dto.getExpiry());
        method.setCardHolderName(dto.getCardHolderName());
        method.setCvv(dto.getCvv());
        method.setBillingAddress(dto.getBillingAddress());
        method.setDefault(false);

        repository.save(method);

        return "Payment method added successfully";
    }
    // View all cards
    public List<PaymentMethod> getMethods(Long userId) {
        return repository.findByUserId(userId);
    }

    // Set default
    @Transactional
    public String setDefault(Long id, Long userId) {

        List<PaymentMethod> methods = repository.findByUserId(userId);

        for (PaymentMethod m : methods) {
            m.setDefault(false);
            repository.save(m);
        }

        PaymentMethod selected = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Card not found"));

        if (!selected.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized");
        }

        selected.setDefault(true);
        repository.save(selected);

        return "Default payment method updated";
    }
    
    //update
    public String update(Long id, Long userId, PaymentMethodDTO dto) {

        PaymentMethod method = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Card not found"));

        if (!method.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized");
        }

        method.setExpiry(dto.getExpiry());
        method.setBillingAddress(dto.getBillingAddress());

        repository.save(method);

        return "Payment method updated";
    }

    // Delete card
    public String delete(Long id, Long userId) {

        PaymentMethod method = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Card not found"));

        if (!method.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized");
        }

        repository.delete(method);

        return "Payment method deleted";
    }
}
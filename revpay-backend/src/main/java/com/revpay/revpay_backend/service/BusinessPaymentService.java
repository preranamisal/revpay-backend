package com.revpay.revpay_backend.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.revpay.revpay_backend.dto.BusinessPaymentMethodDTO;
import com.revpay.revpay_backend.model.*;
import com.revpay.revpay_backend.repository.BusinessPaymentMethodRepository;

@Service
public class BusinessPaymentService {

    private final BusinessPaymentMethodRepository repository;

    public BusinessPaymentService(BusinessPaymentMethodRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public String addMethod(Long userId, BusinessPaymentMethodDTO dto) {

        BusinessPaymentType type =
                BusinessPaymentType.valueOf(dto.getType().toUpperCase());

        BusinessPaymentMethod method = new BusinessPaymentMethod();
        method.setBusinessUserId(userId);
        method.setType(type);
        method.setDefault(false);

        if (type == BusinessPaymentType.CARD) {
            method.setCardNumber(dto.getCardNumber());
            method.setExpiry(dto.getExpiry());
            method.setCardHolderName(dto.getCardHolderName());
        } else {
            method.setBankName(dto.getBankName());
            method.setAccountNumber(dto.getAccountNumber());
            method.setIfscCode(dto.getIfscCode());
        }

        repository.save(method);
        return "Business payment method added successfully";
    }
}
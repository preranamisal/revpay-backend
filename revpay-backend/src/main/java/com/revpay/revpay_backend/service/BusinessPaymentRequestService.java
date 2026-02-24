package com.revpay.revpay_backend.service;

import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

import com.revpay.revpay_backend.model.*;
import com.revpay.revpay_backend.repository.BusinessPaymentRequestRepository;

@Service
public class BusinessPaymentRequestService {

    private final BusinessPaymentRequestRepository repository;

    public BusinessPaymentRequestService(
            BusinessPaymentRequestRepository repository) {
        this.repository = repository;
    }

    public String createRequest(Long businessId,
                                String identifier,
                                Double amount,
                                String description) {

        BusinessPaymentRequest request = new BusinessPaymentRequest();
        request.setBusinessId(businessId);
        request.setCustomerIdentifier(identifier);
        request.setAmount(amount);
        request.setDescription(description);
        request.setStatus("PENDING");
        request.setCreatedAt(LocalDateTime.now());

        repository.save(request);

        return "Payment request generated";
    }

    public String processPayment(Long requestId) {

        BusinessPaymentRequest request = repository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        request.setStatus("PAID");
        repository.save(request);

        return "Payment processed successfully";
    }
}
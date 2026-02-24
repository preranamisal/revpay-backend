package com.revpay.revpay_backend.service;

import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

import com.revpay.revpay_backend.dto.BusinessRegisterDTO;
import com.revpay.revpay_backend.model.*;
import com.revpay.revpay_backend.repository.*;

@Service
public class BusinessService {

    private final BusinessProfileRepository repository;
    private final UserRepository userRepository;

    public BusinessService(BusinessProfileRepository repository,
                           UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    public String createBusinessProfile(Long userId, BusinessRegisterDTO dto) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // ✅ Correct role check
        if (user.getRole() != UserRole.ROLE_BUSINESS) {
            throw new RuntimeException("Only BUSINESS users allowed");
        }

        BusinessProfile profile = new BusinessProfile();
        profile.setUserId(userId);
        profile.setBusinessName(dto.getBusinessName());
        profile.setBusinessType(dto.getBusinessType());
        profile.setTaxId(dto.getTaxId());
        profile.setBusinessAddress(dto.getBusinessAddress());
        profile.setContactNumber(dto.getContactNumber());
        profile.setVerified(false);
        profile.setCreatedAt(LocalDateTime.now());

        repository.save(profile);

        return "Business profile created. Awaiting verification.";
    }
    
    public String submitVerification(Long userId, String documentName) {

        BusinessProfile profile = repository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Business profile not found"));

        if (profile.getStatus() == BusinessStatus.APPROVED) {
            throw new RuntimeException("Business already verified");
        }

        profile.setDocumentName(documentName);
        profile.setStatus(BusinessStatus.PENDING);

        repository.save(profile);

        return "Verification document submitted. Awaiting approval.";
    }
    
}
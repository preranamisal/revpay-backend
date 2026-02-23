package com.revpay.revpay_backend.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.revpay.revpay_backend.dto.*;
import com.revpay.revpay_backend.model.User;
import com.revpay.revpay_backend.repository.UserRepository;

@Service
public class ProfileService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ProfileService(UserRepository userRepository,
                          PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // View profile
    public ProfileResponseDTO getProfile(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new ProfileResponseDTO(
                user.getFullName(),
                user.getEmail(),
                user.getPhone()
        );
    }

    // Update profile
    public String updateProfile(String email, UpdateProfileDTO dto) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (dto.getFullName() != null) {
            user.setFullName(dto.getFullName());
        }

        if (dto.getPhone() != null) {
            user.setPhone(dto.getPhone());
        }

        userRepository.save(user);

        return "Profile updated successfully";
    }

    // Change password
    public String changePassword(String email, ChangePasswordDTO dto) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);

        return "Password changed successfully";
    }
    
 // Set PIN
    public String setPin(String email, SetPinDTO dto) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getTransactionPin() != null) {
            throw new RuntimeException("PIN already set. Use change PIN.");
        }

        user.setTransactionPin(passwordEncoder.encode(dto.getPin()));
        userRepository.save(user);

        return "Transaction PIN set successfully";
    }

    // Change PIN
    public String changePin(String email, ChangePinDTO dto) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(dto.getCurrentPin(), user.getTransactionPin())) {
            throw new RuntimeException("Current PIN is incorrect");
        }

        user.setTransactionPin(passwordEncoder.encode(dto.getNewPin()));
        userRepository.save(user);

        return "Transaction PIN changed successfully";
    }
}
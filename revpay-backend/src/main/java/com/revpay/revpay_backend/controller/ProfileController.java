package com.revpay.revpay_backend.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import com.revpay.revpay_backend.dto.*;
import com.revpay.revpay_backend.service.ProfileService;

@RestController
@RequestMapping("/profile")
public class ProfileController {

    private final ProfileService service;

    public ProfileController(ProfileService service) {
        this.service = service;
    }

    // View profile
    @GetMapping
    public ProfileResponseDTO getProfile(Authentication authentication) {
        return service.getProfile(authentication.getName());
    }

    // Update profile
    @PutMapping("/update")
    public String updateProfile(@RequestBody UpdateProfileDTO dto,
                                Authentication authentication) {
        return service.updateProfile(authentication.getName(), dto);
    }

    // Change password
    @PutMapping("/change-password")
    public String changePassword(@RequestBody ChangePasswordDTO dto,
                                 Authentication authentication) {
        return service.changePassword(authentication.getName(), dto);
    }
    
    @PutMapping("/set-pin")
    public String setPin(@RequestBody SetPinDTO dto,
                         Authentication authentication) {
        return service.setPin(authentication.getName(), dto);
    }

    @PutMapping("/change-pin")
    public String changePin(@RequestBody ChangePinDTO dto,
                            Authentication authentication) {
        return service.changePin(authentication.getName(), dto);
    }
}
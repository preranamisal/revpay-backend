package com.revpay.revpay_backend.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.revpay.revpay_backend.dto.BusinessRegisterDTO;
import com.revpay.revpay_backend.model.User;
import com.revpay.revpay_backend.repository.UserRepository;
import com.revpay.revpay_backend.service.BusinessService;

@RestController
	@RequestMapping("/business")
	public class BusinessController {

	    private final BusinessService service;
	    private final UserRepository userRepository;

	    public BusinessController(BusinessService service,
	                              UserRepository userRepository) {
	        this.service = service;
	        this.userRepository = userRepository;
	    }

	    @PostMapping("/profile")
	    public String createProfile(
	            @RequestBody BusinessRegisterDTO dto,
	            @AuthenticationPrincipal UserDetails userDetails) {

	        User user = userRepository.findByEmail(userDetails.getUsername())
	                .orElseThrow(() -> new RuntimeException("User not found"));

	        return service.createBusinessProfile(user.getId(), dto);
	    }
	}


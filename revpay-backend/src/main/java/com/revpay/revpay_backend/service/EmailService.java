package com.revpay.revpay_backend.service;


import org.springframework.stereotype.Service;

@Service
public class EmailService {

    public void sendEmail(String to, String subject, String body) {

        // Simulated email sending
        System.out.println("📧 Sending Email...");
        System.out.println("To: " + to);
        System.out.println("Subject: " + subject);
        System.out.println("Body: " + body);
    }
}
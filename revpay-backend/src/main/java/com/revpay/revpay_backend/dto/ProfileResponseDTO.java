package com.revpay.revpay_backend.dto;

public class ProfileResponseDTO {

    private String fullName;
    private String email;
    private String phone;

    public ProfileResponseDTO(String fullName, String email, String phone) {
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
    }

    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
}
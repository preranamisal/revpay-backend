package com.revpay.revpay_backend.dto;

import jakarta.validation.constraints.NotBlank;

public class BusinessVerificationDTO {

    @NotBlank
    private String documentName;

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }
}

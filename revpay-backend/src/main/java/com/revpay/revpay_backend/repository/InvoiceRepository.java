package com.revpay.revpay_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


import com.revpay.revpay_backend.model.Invoice;

public interface InvoiceRepository
extends JpaRepository<Invoice, Long> {
	List<Invoice> findByBusinessId(Long businessId);
}


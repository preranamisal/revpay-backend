package com.revpay.revpay_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.revpay.revpay_backend.model.InvoiceItem;

public interface InvoiceItemRepository
extends JpaRepository<InvoiceItem, Long> {
	   List<InvoiceItem> findByInvoiceId(Long invoiceId);
}
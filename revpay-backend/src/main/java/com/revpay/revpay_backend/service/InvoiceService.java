//package com.revpay.revpay_backend.service;
//
//import org.springframework.stereotype.Service;
//import java.time.LocalDateTime;
//
//import com.revpay.revpay_backend.dto.*;
//import com.revpay.revpay_backend.model.*;
//import com.revpay.revpay_backend.repository.*;
//
//@Service
//public class InvoiceService {
//
//    private final InvoiceRepository invoiceRepo;
//    private final InvoiceItemRepository itemRepo;
//
//    public InvoiceService(InvoiceRepository invoiceRepo,
//                          InvoiceItemRepository itemRepo) {
//        this.invoiceRepo = invoiceRepo;
//        this.itemRepo = itemRepo;
//    }
//
//    public String createInvoice(Long businessId, CreateInvoiceDTO dto) {
//
//        Invoice invoice = new Invoice();
//        invoice.setBusinessId(businessId);
//        invoice.setCustomerName(dto.getCustomerName());
//        invoice.setStatus("DRAFT");
//        invoice.setCreatedAt(LocalDateTime.now());
//
//        invoiceRepo.save(invoice);
//
//        double total = 0;
//        double totalTax = 0;
//
//        for (InvoiceItemDTO itemDTO : dto.getItems()) {
//
//            InvoiceItem item = new InvoiceItem();
//            item.setInvoiceId(invoice.getId());
//            item.setDescription(itemDTO.getDescription());
//            item.setQuantity(itemDTO.getQuantity());
//            item.setUnitPrice(itemDTO.getUnitPrice());
//            item.setTax(itemDTO.getTax());
//
//            itemRepo.save(item);
//
//            double itemTotal =
//                    itemDTO.getQuantity() * itemDTO.getUnitPrice();
//
//            total += itemTotal;
//            totalTax += itemDTO.getTax();
//        }
//
//        invoice.setTotalAmount(total + totalTax);
//        invoice.setTotalTax(totalTax);
//
//        invoiceRepo.save(invoice);
//
//        return "Invoice created successfully";
//    }
//}

package com.revpay.revpay_backend.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.revpay.revpay_backend.dto.*;
import com.revpay.revpay_backend.model.*;
import com.revpay.revpay_backend.repository.*;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepo;
    private final InvoiceItemRepository itemRepo;

    public InvoiceService(InvoiceRepository invoiceRepo,
                          InvoiceItemRepository itemRepo) {
        this.invoiceRepo = invoiceRepo;
        this.itemRepo = itemRepo;
    }

    @Transactional
    public String createInvoice(Long businessId, CreateInvoiceDTO dto) {

        Invoice invoice = new Invoice();
        invoice.setBusinessId(businessId);

        invoice.setCustomerName(dto.getCustomerName());
        invoice.setCustomerEmail(dto.getCustomerEmail());
        invoice.setCustomerAddress(dto.getCustomerAddress());

        invoice.setPaymentTerms(dto.getPaymentTerms());
        invoice.setDueDate(dto.getDueDate());

        invoice.setStatus(InvoiceStatus.DRAFT);
        invoice.setCreatedAt(LocalDateTime.now());

        invoiceRepo.save(invoice);

        double total = 0;
        double totalTax = 0;

        for (InvoiceItemDTO itemDTO : dto.getItems()) {

            InvoiceItem item = new InvoiceItem();
            item.setInvoiceId(invoice.getId());
            item.setDescription(itemDTO.getDescription());
            item.setQuantity(itemDTO.getQuantity());
            item.setUnitPrice(itemDTO.getUnitPrice());
            item.setTax(itemDTO.getTax());

            itemRepo.save(item);

            total += itemDTO.getQuantity() * itemDTO.getUnitPrice();
            totalTax += itemDTO.getTax();
        }

        invoice.setTotalAmount(total + totalTax);
        invoice.setTotalTax(totalTax);

        invoiceRepo.save(invoice);

        return "Invoice created successfully";
    }

    public List<Invoice> getAllInvoices(Long businessId) {

        List<Invoice> invoices = invoiceRepo.findByBusinessId(businessId);

        for (Invoice invoice : invoices) {
            if (invoice.getStatus() == InvoiceStatus.SENT &&
                invoice.getDueDate().isBefore(LocalDate.now())) {

                invoice.setStatus(InvoiceStatus.OVERDUE);
                invoiceRepo.save(invoice);
            }
        }

        return invoices;
    }

    public String sendInvoice(Long invoiceId) {

        Invoice invoice = invoiceRepo.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        invoice.setStatus(InvoiceStatus.SENT);
        invoiceRepo.save(invoice);

        return "Invoice sent to customer";
    }

    public String markAsPaid(Long invoiceId) {

        Invoice invoice = invoiceRepo.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        invoice.setStatus(InvoiceStatus.PAID);
        invoiceRepo.save(invoice);

        return "Invoice marked as PAID";
    }

    public String cancelInvoice(Long invoiceId) {

        Invoice invoice = invoiceRepo.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        invoice.setStatus(InvoiceStatus.CANCELLED);
        invoiceRepo.save(invoice);

        return "Invoice cancelled";
    }
}
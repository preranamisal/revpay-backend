package com.revpay.revpay_backend.controller;



import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

import com.revpay.revpay_backend.dto.CreateInvoiceDTO;
import com.revpay.revpay_backend.model.*;
import com.revpay.revpay_backend.repository.UserRepository;
import com.revpay.revpay_backend.service.InvoiceService;

@RestController
@RequestMapping("/business/invoice")
public class InvoiceController {

    private final InvoiceService service;
    private final UserRepository userRepository;

    public InvoiceController(InvoiceService service,
                             UserRepository userRepository) {
        this.service = service;
        this.userRepository = userRepository;
    }

    @PostMapping("/create")
    public String create(
            @RequestBody CreateInvoiceDTO dto,
            @AuthenticationPrincipal UserDetails userDetails) {

        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return service.createInvoice(user.getId(), dto);
    }

    @GetMapping("/all")
    public List<Invoice> getAll(
            @AuthenticationPrincipal UserDetails userDetails) {

        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return service.getAllInvoices(user.getId());
    }

    @PostMapping("/{id}/send")
    public String send(@PathVariable Long id) {
        return service.sendInvoice(id);
    }

    @PostMapping("/{id}/pay")
    public String pay(@PathVariable Long id) {
        return service.markAsPaid(id);
    }

    @PostMapping("/{id}/cancel")
    public String cancel(@PathVariable Long id) {
        return service.cancelInvoice(id);
    }
}

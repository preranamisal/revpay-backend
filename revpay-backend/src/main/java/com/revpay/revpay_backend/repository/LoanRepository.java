package com.revpay.revpay_backend.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.revpay.revpay_backend.model.Loan;

public interface LoanRepository extends JpaRepository<Loan, Long> {

    List<Loan> findByBusinessId(Long businessId);
}

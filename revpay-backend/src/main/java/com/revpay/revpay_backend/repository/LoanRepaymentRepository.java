package com.revpay.revpay_backend.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.revpay.revpay_backend.model.LoanRepayment;

public interface LoanRepaymentRepository
        extends JpaRepository<LoanRepayment, Long> {

    List<LoanRepayment> findByLoanId(Long loanId);
}

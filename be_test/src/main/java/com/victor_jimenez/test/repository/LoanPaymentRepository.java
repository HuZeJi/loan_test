package com.victor_jimenez.test.repository;

import com.victor_jimenez.test.model.Loan;
import com.victor_jimenez.test.model.LoanApplicationStatus;
import com.victor_jimenez.test.model.LoanPayment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanPaymentRepository extends JpaRepository<LoanPayment, String> {
    Page<LoanPayment> findByLoanId(String loanId, Pageable pageable);
}

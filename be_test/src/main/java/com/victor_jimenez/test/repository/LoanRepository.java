package com.victor_jimenez.test.repository;

import com.victor_jimenez.test.model.Loan;
import com.victor_jimenez.test.model.LoanApplicationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, String> {

    Page<Loan> findByClientIdAndActive(String clientId, Boolean isActive, Pageable pageable);

    Page<Loan> findByClientIdAndApplicationStatusAndActive(
            String clientId,
            LoanApplicationStatus applicationStatus,
            Boolean isActive,
            Pageable pageable
    );

    List<Loan> findByClientId(String clientId);
}

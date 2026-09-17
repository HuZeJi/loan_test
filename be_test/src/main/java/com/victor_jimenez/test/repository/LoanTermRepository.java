package com.victor_jimenez.test.repository;

import com.victor_jimenez.test.model.LoanTerm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanTermRepository extends JpaRepository<LoanTerm, String> {
}

package com.victor_jimenez.test.service;

import com.victor_jimenez.test.model.Loan;
import com.victor_jimenez.test.model.LoanPayment;
import com.victor_jimenez.test.model.LoanPaymentDTO;
import com.victor_jimenez.test.model.LoanPaymentStatus;
import com.victor_jimenez.test.model.User;
import com.victor_jimenez.test.repository.LoanPaymentRepository;
import com.victor_jimenez.test.repository.LoanRepository;
import com.victor_jimenez.test.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface LoanPaymentSvc {
    Page<LoanPaymentDTO> listByLoan(String loanId, Pageable pageable);
    LoanPaymentDTO registerPayment(LoanPaymentDTO loanPayment, String loanId);
}

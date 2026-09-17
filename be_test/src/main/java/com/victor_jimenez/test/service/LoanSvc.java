package com.victor_jimenez.test.service;

import com.victor_jimenez.test.model.Client;
import com.victor_jimenez.test.model.Loan;
import com.victor_jimenez.test.model.LoanApplicationStatus;
import com.victor_jimenez.test.model.LoanDTO;
import com.victor_jimenez.test.model.LoanPaymentStatus;
import com.victor_jimenez.test.model.LoanTerm;
import com.victor_jimenez.test.model.User;
import com.victor_jimenez.test.repository.ClientRepository;
import com.victor_jimenez.test.repository.LoanRepository;
import com.victor_jimenez.test.repository.LoanTermRepository;
import com.victor_jimenez.test.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface LoanSvc {

    LoanDTO create(LoanDTO loanRequest);

    Page<LoanDTO> listByClient(String clientId, Pageable pageable);

    Page<LoanDTO> listApprovedByClient(String clientId, Pageable pageable);

    LoanDTO approve(String loanId, String userId, String resolutionNotes);

    LoanDTO reject(String loanId, String userId, String resolutionNotes);
}

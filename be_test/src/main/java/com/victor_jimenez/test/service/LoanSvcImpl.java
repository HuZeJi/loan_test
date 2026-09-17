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

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
class LoanSvcImpl implements LoanSvc {

    private final LoanRepository loanRepository;
    private final ClientRepository clientRepository;
    private final LoanTermRepository loanTermRepository;
    private final UserRepository userRepository;

    public LoanSvcImpl(LoanRepository loanRepository,
                       ClientRepository clientRepository,
                       LoanTermRepository loanTermRepository,
                       UserRepository userRepository) {
        this.loanRepository = loanRepository;
        this.clientRepository = clientRepository;
        this.loanTermRepository = loanTermRepository;
        this.userRepository = userRepository;
    }

    @Override
    public LoanDTO create(LoanDTO loanRequest) {
        Client client = clientRepository.findById(loanRequest.getClientId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado"));
        LoanTerm loanTerm = loanTermRepository.findById(String.valueOf(loanRequest.getLoanTermId()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Plazo no encontrado"));

        Loan loan = loanRequest.toEntity();
        loan.setPendingAmount(loanRequest.getAmount());
        loan.setApplicationStatus(LoanApplicationStatus.PENDING);
        loan.setPaymentStatus(LoanPaymentStatus.PENDING);
        loan.setClient(client);
        loan.setLoanTerm(loanTerm);
        loan.setRequestDate(LocalDateTime.now());

        return LoanDTO.toDTO(loanRepository.save(loan));
    }

    @Override
    public Page<LoanDTO> listByClient(String clientId, Pageable pageable) {
        return loanRepository.findByClientIdAndActive(clientId, Boolean.TRUE, pageable).map(LoanDTO::toDTO);
    }

    @Override
    public Page<LoanDTO> listApprovedByClient(String clientId, Pageable pageable) {
        return loanRepository
                .findByClientIdAndApplicationStatusAndActive(clientId, LoanApplicationStatus.APPROVED, Boolean.TRUE, pageable)
                .map(LoanDTO::toDTO);
    }

    @Override
    public LoanDTO approve(String loanId, String userId, String resolutionNotes) {
        return resolve(loanId, LoanApplicationStatus.APPROVED, userId, resolutionNotes);
    }

    @Override
    public LoanDTO reject(String loanId, String userId, String resolutionNotes) {
        return resolve(loanId, LoanApplicationStatus.REJECTED, userId, resolutionNotes);
    }

    private LoanDTO resolve(String loanId, LoanApplicationStatus status, String userId, String resolutionNotes) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Prestamo no encontrado"));
        if(!loan.getApplicationStatus().equals(LoanApplicationStatus.PENDING)) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_CONTENT, "El prestamo ya fue procesado");
        }

        if (userId != null && !userId.isBlank()) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
            loan.setUser(user);
        }
        loan.setApplicationStatus(status);
        loan.setResolutionDate(LocalDate.now());
        loan.setResolutionNotes(resolutionNotes);

        return LoanDTO.toDTO(loanRepository.save(loan));
    }
}

package com.victor_jimenez.test.service;

import com.victor_jimenez.test.model.Loan;
import com.victor_jimenez.test.model.LoanApplicationStatus;
import com.victor_jimenez.test.model.LoanDTO;
import com.victor_jimenez.test.model.LoanPayment;
import com.victor_jimenez.test.model.LoanPaymentDTO;
import com.victor_jimenez.test.model.LoanPaymentStatus;
import com.victor_jimenez.test.model.User;
import com.victor_jimenez.test.repository.LoanPaymentRepository;
import com.victor_jimenez.test.repository.LoanRepository;
import com.victor_jimenez.test.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
class LoanPaymentSvcImpl implements LoanPaymentSvc{

    private static final Logger log = LoggerFactory.getLogger(LoanPaymentSvcImpl.class);

    private final LoanRepository loanRepository;
    private final LoanPaymentRepository loanPaymentRepository;
    private final UserRepository userRepository;

    public LoanPaymentSvcImpl(LoanRepository loanRepository,
                              LoanPaymentRepository loanPaymentRepository,
                              UserRepository userRepository) {
        this.loanRepository = loanRepository;
        this.loanPaymentRepository = loanPaymentRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Page<LoanPaymentDTO> listByLoan(String loanId, Pageable pageable) {
        return loanPaymentRepository.findByLoanId(loanId, pageable).map(LoanPaymentDTO::toDto);
    }

    @Transactional
    @Override
    public LoanPaymentDTO registerPayment(LoanPaymentDTO loanPayment, String loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "El prestamo asociado no existe"));

        if (loan.getApplicationStatus() != LoanApplicationStatus.APPROVED) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_CONTENT, "El prestamo no esta aprobado");
        }

        if (loan.getPaymentStatus() == LoanPaymentStatus.PAID) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_CONTENT, "El prestamo ya esta solventado");
        }

        if (loanPayment.getAmount().compareTo(loan.getPendingAmount()) > 0) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_CONTENT, "El pago es mayor al monto pendiente del prestamo");
        }

        User user = userRepository.findById(loanPayment.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        LoanPayment payment = loanPayment.toEntity();
        payment.setLoan(loan);
        payment.setUser(user);
        payment.setPaymentDate(loanPayment.getPaymentDate() != null ? loanPayment.getPaymentDate() : LocalDate.now());
        LoanPayment savedLoanPayment = loanPaymentRepository.save(payment);

        BigDecimal newPendingAmount = loan.getPendingAmount().subtract(loanPayment.getAmount());
        loan.setPendingAmount(newPendingAmount);
        loan.setPaymentStatus(newPendingAmount.compareTo(BigDecimal.ZERO) == 0
                ? LoanPaymentStatus.PAID
                : LoanPaymentStatus.PARTIALLY_PAID);
        loanRepository.save(loan);

        log.info("Pago registrado: loanId={}, paymentId={}, amount={}, pendingAmount={}, paymentStatus={}",
                loanId, savedLoanPayment.getId(), savedLoanPayment.getAmount(), newPendingAmount, loan.getPaymentStatus());
        return LoanPaymentDTO.toDto(savedLoanPayment);
    }
}

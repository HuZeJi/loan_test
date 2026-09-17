package com.victor_jimenez.test.service;

import com.victor_jimenez.test.model.Loan;
import com.victor_jimenez.test.model.LoanApplicationStatus;
import com.victor_jimenez.test.model.LoanPayment;
import com.victor_jimenez.test.model.LoanPaymentDTO;
import com.victor_jimenez.test.model.LoanPaymentStatus;
import com.victor_jimenez.test.model.User;
import com.victor_jimenez.test.repository.LoanPaymentRepository;
import com.victor_jimenez.test.repository.LoanRepository;
import com.victor_jimenez.test.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoanPaymentSvcImplTest {

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private LoanPaymentRepository loanPaymentRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private LoanPaymentSvcImpl loanPaymentSvc;

    private Loan loan;
    private User user;

    @BeforeEach
    void setUp() {
        loan = new Loan();
        loan.setId("loan-1");
        loan.setAmount(new BigDecimal("1000.00"));
        loan.setPendingAmount(new BigDecimal("1000.00"));
        loan.setPaymentStatus(LoanPaymentStatus.PENDING);

        user = new User();
        user.setId("user-1");
        user.setUsername("cajero");
        user.setRole("USER");
    }

    private LoanPaymentDTO newPaymentRequest(String amount) {
        LoanPaymentDTO dto = new LoanPaymentDTO();
        dto.setAmount(new BigDecimal(amount));
        dto.setPaymentMethod("TRANSFERENCIA");
        dto.setUserId("user-1");
        return dto;
    }

    @Test
    void when_listByLoan_ok() {
        Pageable pageable = Pageable.unpaged();
        LoanPayment payment = new LoanPayment();
        payment.setId("payment-1");
        payment.setLoan(loan);
        payment.setUser(user);
        payment.setAmount(new BigDecimal("100.00"));
        payment.setPaymentMethod("EFECTIVO");
        payment.setPaymentDate(LocalDate.now());
        Page<LoanPayment> page = new PageImpl<>(List.of(payment));
        when(loanPaymentRepository.findByLoanId("loan-1", pageable)).thenReturn(page);

        Page<LoanPaymentDTO> result = loanPaymentSvc.listByLoan("loan-1", pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getId()).isEqualTo("payment-1");
    }

    @Test
    void when_registerPayment_ok() {
        loan.setApplicationStatus(LoanApplicationStatus.APPROVED);
        when(loanRepository.findById("loan-1")).thenReturn(Optional.of(loan));
        when(userRepository.findById("user-1")).thenReturn(Optional.of(user));
        when(loanPaymentRepository.save(any(LoanPayment.class))).thenAnswer(inv -> inv.getArgument(0));

        LoanPaymentDTO result = loanPaymentSvc.registerPayment(newPaymentRequest("400.00"), "loan-1");

        assertThat(result.getAmount()).isEqualByComparingTo("400.00");
        assertThat(loan.getPendingAmount()).isEqualByComparingTo("600.00");
        assertThat(loan.getPaymentStatus()).isEqualTo(LoanPaymentStatus.PARTIALLY_PAID);
        verify(loanRepository).save(loan);
    }

    @Test
    void when_registerPayment_ok_full_pay() {
        loan.setApplicationStatus(LoanApplicationStatus.APPROVED);
        when(loanRepository.findById("loan-1")).thenReturn(Optional.of(loan));
        when(userRepository.findById("user-1")).thenReturn(Optional.of(user));
        when(loanPaymentRepository.save(any(LoanPayment.class))).thenAnswer(inv -> inv.getArgument(0));

        loanPaymentSvc.registerPayment(newPaymentRequest("1000.00"), "loan-1");

        assertThat(loan.getPendingAmount()).isEqualByComparingTo("0.00");
        assertThat(loan.getPaymentStatus()).isEqualTo(LoanPaymentStatus.PAID);
    }

    @Test
    void when_registerPayment_no_load_found() {
        when(loanRepository.findById("missing")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> loanPaymentSvc.registerPayment(newPaymentRequest("100.00"), "missing"))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("no existe");

        verify(loanPaymentRepository, never()).save(any());
    }

    @Test
    void when_registerPayment_loan_paid() {
        loan.setApplicationStatus(LoanApplicationStatus.APPROVED);
        loan.setPaymentStatus(LoanPaymentStatus.PAID);
        when(loanRepository.findById("loan-1")).thenReturn(Optional.of(loan));

        assertThatThrownBy(() -> loanPaymentSvc.registerPayment(newPaymentRequest("100.00"), "loan-1"))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("ya esta solventado");

        verify(loanPaymentRepository, never()).save(any());
    }

    @Test
    void when_registerPayment_limit_exceed() {
        loan.setApplicationStatus(LoanApplicationStatus.APPROVED);
        when(loanRepository.findById("loan-1")).thenReturn(Optional.of(loan));

        assertThatThrownBy(() -> loanPaymentSvc.registerPayment(newPaymentRequest("1500.00"), "loan-1"))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("mayor al monto pendiente");

        verify(loanPaymentRepository, never()).save(any());
    }

    @Test
    void when_registerPayment_no_user() {
        loan.setApplicationStatus(LoanApplicationStatus.APPROVED);
        when(loanRepository.findById("loan-1")).thenReturn(Optional.of(loan));
        when(userRepository.findById("user-1")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> loanPaymentSvc.registerPayment(newPaymentRequest("100.00"), "loan-1"))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Usuario no encontrado");

        verify(loanPaymentRepository, never()).save(any());
    }
}

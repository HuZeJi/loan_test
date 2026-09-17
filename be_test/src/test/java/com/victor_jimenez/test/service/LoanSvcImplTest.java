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
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoanSvcImplTest {

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private LoanTermRepository loanTermRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private LoanSvcImpl loanSvc;

    private Client client;
    private LoanTerm loanTerm;
    private Loan loan;
    private User user;

    @BeforeEach
    void setUp() {
        client = new Client();
        client.setId("client-1");
        client.setActive(true);

        loanTerm = new LoanTerm();
        loanTerm.setId("term-1");
        loanTerm.setDescription("12 meses");
        loanTerm.setDays(360);

        loan = new Loan();
        loan.setId("loan-1");
        loan.setAmount(new BigDecimal("5000.00"));
        loan.setPendingAmount(new BigDecimal("5000.00"));
        loan.setApplicationStatus(LoanApplicationStatus.PENDING);
        loan.setPaymentStatus(LoanPaymentStatus.PENDING);
        loan.setClient(client);
        loan.setLoanTerm(loanTerm);
        loan.setActive(true);

        user = new User();
        user.setId("user-1");
        user.setUsername("analista");
        user.setRole("USER");
    }

    private LoanDTO newLoanRequest() {
        LoanDTO dto = new LoanDTO();
        dto.setAmount(new BigDecimal("5000.00"));
        dto.setClientId("client-1");
        dto.setLoanTermId("term-1");
        return dto;
    }

    @Test
    void when_create_ok() {
        when(clientRepository.findById("client-1")).thenReturn(Optional.of(client));
        when(loanTermRepository.findById("term-1")).thenReturn(Optional.of(loanTerm));
        when(loanRepository.save(any(Loan.class))).thenAnswer(inv -> inv.getArgument(0));

        LoanDTO result = loanSvc.create(newLoanRequest());

        assertThat(result.getApplicationStatus()).isEqualTo(LoanApplicationStatus.PENDING);
        assertThat(result.getPaymentStatus()).isEqualTo(LoanPaymentStatus.PENDING);
        assertThat(result.getPendingAmount()).isEqualByComparingTo("5000.00");
        assertThat(result.getActive()).isTrue();
        assertThat(result.getClientId()).isEqualTo("client-1");
        assertThat(result.getLoanTermId()).isEqualTo("term-1");
    }

    @Test
    void when_create_no_client_found() {
        when(clientRepository.findById("client-1")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> loanSvc.create(newLoanRequest()))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Cliente no encontrado");

        verify(loanRepository, never()).save(any());
    }

    @Test
    void when_create_no_loan_term() {
        when(clientRepository.findById("client-1")).thenReturn(Optional.of(client));
        when(loanTermRepository.findById("term-1")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> loanSvc.create(newLoanRequest()))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Plazo no encontrado");

        verify(loanRepository, never()).save(any());
    }

    @Test
    void when_listByClient_ok() {
        Pageable pageable = Pageable.unpaged();
        Page<Loan> page = new PageImpl<>(List.of(loan));
        when(loanRepository.findByClientIdAndActive("client-1", true, pageable)).thenReturn(page);

        Page<LoanDTO> result = loanSvc.listByClient("client-1", pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getId()).isEqualTo("loan-1");
    }

    @Test
    void when_listApprovedByClient_approved_ok() {
        Pageable pageable = Pageable.unpaged();
        Page<Loan> page = new PageImpl<>(List.of(loan));
        when(loanRepository.findByClientIdAndApplicationStatusAndActive(
                "client-1", LoanApplicationStatus.APPROVED, true, pageable)).thenReturn(page);

        Page<LoanDTO> result = loanSvc.listApprovedByClient("client-1", pageable);

        assertThat(result.getContent()).hasSize(1);
    }

    @Test
    void when_approve_ok() {
        when(loanRepository.findById("loan-1")).thenReturn(Optional.of(loan));
        when(userRepository.findById("user-1")).thenReturn(Optional.of(user));
        when(loanRepository.save(any(Loan.class))).thenAnswer(inv -> inv.getArgument(0));

        LoanDTO result = loanSvc.approve("loan-1", "user-1", "Buen historial crediticio");

        assertThat(result.getApplicationStatus()).isEqualTo(LoanApplicationStatus.APPROVED);
        assertThat(result.getResolutionNotes()).isEqualTo("Buen historial crediticio");
        assertThat(result.getResolutionDate()).isNotNull();
        assertThat(loan.getUser()).isEqualTo(user);
    }

    @Test
    void when_approve_no_user() {
        when(loanRepository.findById("loan-1")).thenReturn(Optional.of(loan));
        when(loanRepository.save(any(Loan.class))).thenAnswer(inv -> inv.getArgument(0));

        loanSvc.approve("loan-1", null, "Sin usuario asignado");

        assertThat(loan.getUser()).isNull();
        verify(userRepository, never()).findById(any());
    }

    @Test
    void when_approve_no_loan() {
        when(loanRepository.findById("missing")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> loanSvc.approve("missing", null, null))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Prestamo no encontrado");
    }

    @Test
    void when_approve_loan_is_not_pending() {
        loan.setApplicationStatus(LoanApplicationStatus.APPROVED);
        when(loanRepository.findById("loan-1")).thenReturn(Optional.of(loan));

        assertThatThrownBy(() -> loanSvc.approve("loan-1", null, null))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("ya fue procesado");

        verify(loanRepository, never()).save(any());
    }

    @Test
    void when_approve_no_db_user() {
        when(loanRepository.findById("loan-1")).thenReturn(Optional.of(loan));
        when(userRepository.findById("missing-user")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> loanSvc.approve("loan-1", "missing-user", null))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Usuario no encontrado");

        verify(loanRepository, never()).save(any());
    }

    @Test
    void when_reject_ok() {
        when(loanRepository.findById("loan-1")).thenReturn(Optional.of(loan));
        when(loanRepository.save(any(Loan.class))).thenAnswer(inv -> inv.getArgument(0));

        LoanDTO result = loanSvc.reject("loan-1", null, "No cumple requisitos");

        assertThat(result.getApplicationStatus()).isEqualTo(LoanApplicationStatus.REJECTED);
        assertThat(result.getResolutionNotes()).isEqualTo("No cumple requisitos");
    }

    @Test
    void when_reject_non_pending() {
        loan.setApplicationStatus(LoanApplicationStatus.REJECTED);
        when(loanRepository.findById("loan-1")).thenReturn(Optional.of(loan));

        assertThatThrownBy(() -> loanSvc.reject("loan-1", null, null))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("ya fue procesado");

        verify(loanRepository, never()).save(any());
    }
}

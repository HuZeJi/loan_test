package com.victor_jimenez.test.controller;

import com.victor_jimenez.test.model.LoanDTO;
import com.victor_jimenez.test.model.LoanPaymentDTO;
import com.victor_jimenez.test.service.LoanPaymentSvc;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
public class LoanPaymentController {

    private final LoanPaymentSvc loanPaymentSvc;

    public LoanPaymentController(LoanPaymentSvc loanPaymentService) {
        this.loanPaymentSvc = loanPaymentService;
    }

    @GetMapping("/loan/{loanId}/loan-payment")
    public ResponseEntity<Page<LoanPaymentDTO>> listByLoan(
            @PathVariable String loanId,
            Pageable pageable
    ) {
        return ResponseEntity.ok(loanPaymentSvc.listByLoan(loanId, pageable));
    }

    @PostMapping("/loan/{loanId}/loan-payment")
    public ResponseEntity<LoanPaymentDTO> registerPayment(
            @PathVariable String loanId,
            @Valid @RequestBody LoanPaymentDTO request
    ) {
        return ResponseEntity.ok(loanPaymentSvc.registerPayment(request, loanId));
    }
}

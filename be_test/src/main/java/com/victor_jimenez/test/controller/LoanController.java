package com.victor_jimenez.test.controller;

import com.victor_jimenez.test.model.Loan;
import com.victor_jimenez.test.model.LoanDTO;
import com.victor_jimenez.test.service.LoanSvc;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
@RestController
public class LoanController {

    private final LoanSvc loanSvc;

    public LoanController(LoanSvc loanSvc) {
        this.loanSvc = loanSvc;
    }

    @PostMapping("/loan")
    public ResponseEntity<LoanDTO> create(@RequestBody @Valid LoanDTO request) {
        LoanDTO saved = loanSvc.create(request);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/client/{clientId}/loan")
    public ResponseEntity<Page<LoanDTO>> listByClient(@PathVariable String clientId, Pageable pageable) {
        return ResponseEntity.ok(loanSvc.listByClient(clientId, pageable));
    }

    @GetMapping("/client/{clientId}/loan/approved")
    public ResponseEntity<Page<LoanDTO>> listApprovedByClient(@PathVariable String clientId, Pageable pageable) {
        return ResponseEntity.ok(loanSvc.listApprovedByClient(clientId, pageable));
    }

    @PatchMapping("/loan/{loanId}/approve")
    public ResponseEntity<LoanDTO> approve(
            @PathVariable String loanId,
            @RequestBody(required = false) LoanResolutionRequest request
    ) {
        LoanResolutionRequest body = request != null ? request : LoanResolutionRequest.empty();
        LoanDTO loan = loanSvc.approve(loanId, body.userId(), body.resolutionNotes());
        return ResponseEntity.status(HttpStatus.CREATED).body(loan);
    }

    @PatchMapping("/loan/{loanId}/reject")
    public ResponseEntity<LoanDTO> reject(
            @PathVariable String loanId,
            @RequestBody(required = false) LoanResolutionRequest request
    ) {
        LoanResolutionRequest body = request != null ? request : LoanResolutionRequest.empty();
        LoanDTO loan = loanSvc.reject(loanId, body.userId(), body.resolutionNotes());
        return ResponseEntity.status(HttpStatus.CREATED).body(loan);
    }

    public record LoanResolutionRequest(String userId, String resolutionNotes) {
        static LoanResolutionRequest empty() {
            return new LoanResolutionRequest(null, null);
        }
    }
}

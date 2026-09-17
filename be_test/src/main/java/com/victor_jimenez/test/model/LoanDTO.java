package com.victor_jimenez.test.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class LoanDTO {

    private String id;

    @NotNull
    @Positive
    private BigDecimal amount;

    private BigDecimal pendingAmount;

    private LoanApplicationStatus applicationStatus = LoanApplicationStatus.PENDING;

    private LoanPaymentStatus paymentStatus = LoanPaymentStatus.PENDING;
    private LocalDateTime requestDate;

    private String resolutionNotes;

    private LocalDate resolutionDate;
    private String loanTermId;
    private String clientId;
    private Boolean active;

    public Loan toEntity(){
        Loan loan = new Loan();
        loan.setId(this.id);
        loan.setAmount(this.amount);
        loan.setRequestDate(this.requestDate);
        loan.setPendingAmount(this.pendingAmount);
        loan.setApplicationStatus(this.applicationStatus);
        loan.setPaymentStatus(this.paymentStatus);
        loan.setResolutionNotes(this.resolutionNotes);
        loan.setResolutionDate(this.resolutionDate);

        loan.setActive(this.active);
        return loan;
    }

    public static LoanDTO toDTO(Loan loan){
        LoanDTO loanDTO = new LoanDTO();
        loanDTO.setId(loan.getId());
        loanDTO.setAmount(loan.getAmount());
        loanDTO.setPendingAmount(loan.getPendingAmount());
        loanDTO.setApplicationStatus(loan.getApplicationStatus());
        loanDTO.setPaymentStatus(loan.getPaymentStatus());
        loanDTO.setResolutionNotes(loan.getResolutionNotes());
        loanDTO.setResolutionDate(loan.getResolutionDate());
        loanDTO.setLoanTermId(loan.getLoanTerm().getId());
        loanDTO.setClientId(loan.getClient().getId());
        loanDTO.setActive(loan.getActive());
        return loanDTO;
    }
}

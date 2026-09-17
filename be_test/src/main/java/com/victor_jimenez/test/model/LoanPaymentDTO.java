package com.victor_jimenez.test.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class LoanPaymentDTO {
    private String id;
    private LocalDate paymentDate;
    @NotNull
    private String paymentMethod;
    @NotNull
    private BigDecimal amount;
    private String loanId;
    @NotNull
    private String userId;

    public LoanPayment toEntity() {
        LoanPayment loanPayment = new LoanPayment();
        loanPayment.setId(this.id);
        loanPayment.setPaymentDate(this.paymentDate);
        loanPayment.setPaymentMethod(this.paymentMethod);
        loanPayment.setAmount(this.amount);
        return loanPayment;
    }

    public static LoanPaymentDTO toDto(LoanPayment payment) {
        LoanPaymentDTO loanPayment = new LoanPaymentDTO();
        loanPayment.setId(payment.getId());
        loanPayment.setPaymentDate(payment.getPaymentDate());
        loanPayment.setPaymentMethod(payment.getPaymentMethod());
        loanPayment.setAmount(payment.getAmount());
        loanPayment.setLoanId(payment.getLoan().getId());
        loanPayment.setUserId(payment.getUser().getId());
        return loanPayment;

    }
}

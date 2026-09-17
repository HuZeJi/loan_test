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
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Schema(description = "Datos de un pago registrado sobre un prestamo")
public class LoanPaymentDTO {

    @Schema(description = "Identificador unico del pago (UUID). Se ignora al crear.", example = "e6c4f5d3-3456-4f89-9abc-1234567890gh")
    private String id;

    @Schema(description = "Fecha en que se realizo el pago", example = "2026-02-10")
    private LocalDate paymentDate;

    @NotNull
    @Schema(description = "Metodo de pago utilizado", example = "TRANSFERENCIA")
    private String paymentMethod;

    @NotNull
    @Schema(description = "Monto abonado en este pago", example = "500.00")
    private BigDecimal amount;

    @Schema(description = "Identificador del prestamo al que se abona (se toma del path en el registro)", example = "c4a2d3b1-5678-4d67-9abc-1234567890cd")
    private String loanId;

    @NotNull
    @Schema(description = "Identificador del usuario que registra el pago (UUID)", example = "f7d5g6e4-4567-4g90-9abc-1234567890hi")
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

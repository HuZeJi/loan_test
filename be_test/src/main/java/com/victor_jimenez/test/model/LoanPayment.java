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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Pago registrado sobre un {@link Loan}. Cada pago reduce el pendingAmount del
 * prestamo. Ver docs/DIAGRAMS.md - "Registrar pagos".
 */
@Entity
@Table(name = "loan_payment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Pago registrado sobre un prestamo")
public class LoanPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(description = "Identificador unico del pago (UUID)", example = "e6c4f5d3-3456-4f89-9abc-1234567890gh")
    private String id;

    @NotNull
    @Column(name = "payment_date", nullable = false)
    @Schema(description = "Fecha en que se realizo el pago", example = "2026-02-10")
    private LocalDate paymentDate;

    // TODO: Change this to a enum
    @NotNull
    @Column(name = "payment_method", nullable = false)
    @Schema(description = "Metodo de pago utilizado", example = "TRANSFERENCIA")
    private String paymentMethod;

    @NotNull
    @Column(nullable = false)
    @Schema(description = "Monto abonado en este pago", example = "500.00")
    private BigDecimal amount;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "loan_id", nullable = false)
    @Schema(description = "Prestamo al que se abona este pago")
    private Loan loan;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @Schema(description = "Usuario que registro el pago")
    private User user;
}

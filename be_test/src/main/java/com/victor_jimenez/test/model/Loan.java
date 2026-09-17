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
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Solicitud de prestamo de un {@link Client}. Pasa de PENDING a APROBADO/RECHAZADO
 * y acumula pagos ({@link LoanPayment}) que reducen pendingAmount.
 * Ver docs/DIAGRAMS.md - "Solicitar prestamo" y "Aprobar/rechazar prestamo".
 */
@Entity
@Table(name = "loan")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Solicitud de prestamo asociada a un cliente")
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(description = "Identificador unico del prestamo (UUID)", example = "c4a2d3b1-5678-4d67-9abc-1234567890cd")
    private String id;

    @NotNull
    @Positive
    @Column(nullable = false)
    @Schema(description = "Monto solicitado del prestamo, debe ser positivo", example = "5000.00")
    private BigDecimal amount;

    @Column(name = "pending_amount", nullable = false)
    @Schema(description = "Saldo pendiente por pagar. Disminuye con cada pago registrado.", example = "5000.00")
    private BigDecimal pendingAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "application_status", nullable = false)
    @Schema(description = "Estado de la solicitud dentro del flujo de aprobacion")
    private LoanApplicationStatus applicationStatus = LoanApplicationStatus.PENDING;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    @Schema(description = "Estado de pago segun el saldo pendiente")
    private LoanPaymentStatus paymentStatus = LoanPaymentStatus.PENDING;

    @Column(name = "request_date")
    @Schema(description = "Fecha y hora en que se solicito el prestamo", example = "2026-01-15T10:30:00")
    private LocalDateTime requestDate;

    @Column(name = "resolution_notes")
    @Schema(description = "Notas escritas por el usuario que aprueba o rechaza la solicitud", example = "Cliente con buen historial crediticio")
    private String resolutionNotes;

    @Column(name = "resolution_date")
    @Schema(description = "Fecha en que se aprobo o rechazo la solicitud", example = "2026-01-16")
    private LocalDate resolutionDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    @Schema(description = "Cliente titular de la solicitud de prestamo")
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "loan_term_id", nullable = false)
    @Schema(description = "Plazo del prestamo (duracion en dias)")
    private LoanTerm loanTerm;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @Schema(description = "Usuario que aprobo o rechazo la solicitud (nulo mientras esta pendiente)")
    private User user;

    @Column(name = "is_active", nullable = false)
    @Schema(description = "Indica si el prestamo esta activo. Se desactiva en cascada al desactivar el cliente.", example = "true")
    private Boolean active = true;
}

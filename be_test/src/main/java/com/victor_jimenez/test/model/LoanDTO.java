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
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Schema(description = "Datos de una solicitud de prestamo para crear o consultar")
public class LoanDTO {

    @Schema(description = "Identificador unico del prestamo (UUID). Se ignora al crear.", example = "c4a2d3b1-5678-4d67-9abc-1234567890cd")
    private String id;

    @NotNull
    @Positive
    @Schema(description = "Monto solicitado del prestamo, debe ser positivo", example = "5000.00")
    private BigDecimal amount;

    @Schema(description = "Saldo pendiente por pagar", example = "5000.00")
    private BigDecimal pendingAmount;

    @Schema(description = "Estado de la solicitud dentro del flujo de aprobacion")
    private LoanApplicationStatus applicationStatus = LoanApplicationStatus.PENDING;

    @Schema(description = "Estado de pago segun el saldo pendiente")
    private LoanPaymentStatus paymentStatus = LoanPaymentStatus.PENDING;

    @Schema(description = "Fecha y hora en que se solicito el prestamo", example = "2026-01-15T10:30:00")
    private LocalDateTime requestDate;

    @Schema(description = "Notas de aprobacion o rechazo de la solicitud", example = "Cliente con buen historial crediticio")
    private String resolutionNotes;

    @Schema(description = "Fecha en que se aprobo o rechazo la solicitud", example = "2026-01-16")
    private LocalDate resolutionDate;

    @Schema(description = "Identificador del plazo de prestamo (UUID)", example = "d5b3e4c2-9012-4e78-9abc-1234567890ef")
    private String loanTermId;

    @Schema(description = "Identificador del cliente titular de la solicitud (UUID)", example = "b3f1c2a0-1234-4c56-9abc-1234567890ab")
    private String clientId;

    @Schema(description = "Indica si el prestamo esta activo", example = "true")
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

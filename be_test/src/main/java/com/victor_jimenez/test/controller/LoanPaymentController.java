package com.victor_jimenez.test.controller;

import com.victor_jimenez.test.model.LoanPaymentDTO;
import com.victor_jimenez.test.service.LoanPaymentSvc;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Pagos de prestamo", description = "Registro y consulta de pagos realizados sobre un prestamo")
public class LoanPaymentController {

    private final LoanPaymentSvc loanPaymentSvc;

    public LoanPaymentController(LoanPaymentSvc loanPaymentService) {
        this.loanPaymentSvc = loanPaymentService;
    }

    @GetMapping("/loan/{loanId}/loan-payment")
    @Operation(summary = "Listar pagos de un prestamo", description = "Devuelve, de forma paginada, los pagos registrados sobre un prestamo.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado de pagos obtenido exitosamente"),
            @ApiResponse(responseCode = "401", description = "No autenticado")
    })
    public ResponseEntity<Page<LoanPaymentDTO>> listByLoan(
            @Parameter(description = "Identificador del prestamo (UUID)") @PathVariable String loanId,
            Pageable pageable
    ) {
        return ResponseEntity.ok(loanPaymentSvc.listByLoan(loanId, pageable));
    }

    @PostMapping("/loan/{loanId}/loan-payment")
    @Operation(
            summary = "Registrar un pago sobre un prestamo",
            description = "Registra un pago y reduce el saldo pendiente (pendingAmount) del prestamo. "
                    + "Cuando el saldo llega a cero el prestamo pasa a estado de pago PAID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pago registrado exitosamente",
                    content = @Content(schema = @Schema(implementation = LoanPaymentDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos del pago invalidos"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "404", description = "Prestamo no encontrado")
    })
    public ResponseEntity<LoanPaymentDTO> registerPayment(
            @Parameter(description = "Identificador del prestamo (UUID)") @PathVariable String loanId,
            @Valid @RequestBody LoanPaymentDTO request
    ) {
        return ResponseEntity.ok(loanPaymentSvc.registerPayment(request, loanId));
    }
}

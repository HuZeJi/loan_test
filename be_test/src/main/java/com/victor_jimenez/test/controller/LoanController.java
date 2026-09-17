package com.victor_jimenez.test.controller;

import com.victor_jimenez.test.model.LoanDTO;
import com.victor_jimenez.test.service.LoanSvc;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Prestamos", description = "Solicitud, consulta y aprobacion/rechazo de prestamos")
public class LoanController {

    private final LoanSvc loanSvc;

    public LoanController(LoanSvc loanSvc) {
        this.loanSvc = loanSvc;
    }

    @PostMapping("/loan")
    @Operation(
            summary = "Solicitar un prestamo",
            description = "Crea una nueva solicitud de prestamo para un cliente"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Solicitud de prestamo creada exitosamente",
                    content = @Content(schema = @Schema(implementation = LoanDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos de la solicitud invalidos"),
            @ApiResponse(responseCode = "401", description = "No autenticado")
    })
    public ResponseEntity<LoanDTO> create(@RequestBody @Valid LoanDTO request) {
        LoanDTO saved = loanSvc.create(request);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/client/{clientId}/loan")
    @Operation(summary = "Listar prestamos de un cliente", description = "Devuelve, de forma paginada, todas las solicitudes de prestamo de un cliente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado de prestamos obtenido exitosamente"),
            @ApiResponse(responseCode = "401", description = "No autenticado")
    })
    public ResponseEntity<Page<LoanDTO>> listByClient(
            @Parameter(description = "Identificador del cliente (UUID)") @PathVariable String clientId,
            Pageable pageable
    ) {
        return ResponseEntity.ok(loanSvc.listByClient(clientId, pageable));
    }

    @GetMapping("/client/{clientId}/loan/approved")
    @Operation(summary = "Listar prestamos aprobados de un cliente", description = "Devuelve, de forma paginada, solo las solicitudes aprobadas de prestamo de un cliente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado de prestamos aprobados obtenido exitosamente"),
            @ApiResponse(responseCode = "401", description = "No autenticado")
    })
    public ResponseEntity<Page<LoanDTO>> listApprovedByClient(
            @Parameter(description = "Identificador del cliente (UUID)") @PathVariable String clientId,
            Pageable pageable
    ) {
        return ResponseEntity.ok(loanSvc.listApprovedByClient(clientId, pageable));
    }

    @PatchMapping("/loan/{loanId}/approve")
    @Operation(
            summary = "Aprobar una solicitud de prestamo",
            description = "Cambia el estado de la solicitud a APPROVED y registra el usuario y las notas de resolucion."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Prestamo aprobado exitosamente",
                    content = @Content(schema = @Schema(implementation = LoanDTO.class))),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "404", description = "Prestamo no encontrado")
    })
    public ResponseEntity<LoanDTO> approve(
            @Parameter(description = "Identificador del prestamo (UUID)") @PathVariable String loanId,
            @RequestBody(required = false) LoanResolutionRequest request
    ) {
        LoanResolutionRequest body = request != null ? request : LoanResolutionRequest.empty();
        LoanDTO loan = loanSvc.approve(loanId, body.userId(), body.resolutionNotes());
        return ResponseEntity.status(HttpStatus.CREATED).body(loan);
    }

    @PatchMapping("/loan/{loanId}/reject")
    @Operation(
            summary = "Rechazar una solicitud de prestamo",
            description = "Cambia el estado de la solicitud a REJECTED y registra el usuario y las notas de resolucion."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Prestamo rechazado exitosamente",
                    content = @Content(schema = @Schema(implementation = LoanDTO.class))),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "404", description = "Prestamo no encontrado")
    })
    public ResponseEntity<LoanDTO> reject(
            @Parameter(description = "Identificador del prestamo (UUID)") @PathVariable String loanId,
            @RequestBody(required = false) LoanResolutionRequest request
    ) {
        LoanResolutionRequest body = request != null ? request : LoanResolutionRequest.empty();
        LoanDTO loan = loanSvc.reject(loanId, body.userId(), body.resolutionNotes());
        return ResponseEntity.status(HttpStatus.CREATED).body(loan);
    }

    @Schema(description = "Datos opcionales para resolver (aprobar o rechazar) una solicitud de prestamo")
    public record LoanResolutionRequest(
            @Schema(description = "Identificador del usuario que resuelve la solicitud (UUID)", example = "f7d5g6e4-4567-4g90-9abc-1234567890hi")
            String userId,
            @Schema(description = "Notas de la resolucion", example = "Cliente con buen historial crediticio")
            String resolutionNotes
    ) {
        static LoanResolutionRequest empty() {
            return new LoanResolutionRequest(null, null);
        }
    }
}

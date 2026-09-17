package com.victor_jimenez.test.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Estado de la solicitud de un prestamo dentro del flujo de aprobacion")
public enum LoanApplicationStatus {
    @Schema(description = "Solicitud creada, en espera de resolucion")
    PENDING,
    @Schema(description = "Solicitud aprobada por un usuario")
    APPROVED,
    @Schema(description = "Solicitud rechazada por un usuario")
    REJECTED
}

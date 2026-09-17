package com.victor_jimenez.test.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Estado de pago de un prestamo, segun el saldo pendiente")
public enum LoanPaymentStatus {
    @Schema(description = "Aun no se ha registrado ningun pago")
    PENDING,
    @Schema(description = "Se han registrado pagos, pero queda saldo pendiente")
    PARTIALLY_PAID,
    @Schema(description = "El prestamo esta completamente pagado")
    PAID
}

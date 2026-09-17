package com.victor_jimenez.test.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatusCode;

import java.time.Instant;

@Schema(description = "Cuerpo estandar de error devuelto por la API")
public record ErrorResponse(
        @Schema(description = "Momento en que ocurrio el error") Instant timestamp,
        @Schema(description = "Codigo de estado HTTP", example = "404") int status,
        @Schema(description = "Nombre del estado HTTP", example = "Not Found") String error,
        @Schema(description = "Mensaje descriptivo del error", example = "Cliente no encontrado") String message,
        @Schema(description = "Ruta donde ocurrio el error", example = "/client/123") String path
) {
    public static ErrorResponse of(HttpStatusCode status, String message, String path) {
        return new ErrorResponse(Instant.now(), status.value(), reasonPhrase(status), message, path);
    }

    private static String reasonPhrase(HttpStatusCode status) {
        return org.springframework.http.HttpStatus.resolve(status.value()) != null
                ? org.springframework.http.HttpStatus.resolve(status.value()).getReasonPhrase()
                : String.valueOf(status.value());
    }
}

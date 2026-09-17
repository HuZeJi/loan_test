package com.victor_jimenez.test.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Datos del usuario autenticado")
public class UserInfoResponse {

    @Schema(description = "Identificador unico del usuario (UUID)", example = "f7d5g6e4-4567-4g90-9abc-1234567890hi")
    private String id;

    @Schema(description = "Nombre de usuario", example = "analista1")
    private String username;

    @Schema(description = "Correo electronico del usuario", example = "analista1@example.com")
    private String email;

    @Schema(description = "Rol asignado al usuario", example = "USER")
    private String role;
}

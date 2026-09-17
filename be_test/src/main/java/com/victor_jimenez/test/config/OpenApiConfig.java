package com.victor_jimenez.test.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "API de Prestamos Bancarios",
                version = "v1",
                description = "API REST para la gestion de clientes, solicitudes de prestamo y pagos. "
                        + "Ver docs/REQUIREMENTS.md y docs/DIAGRAMS.md para el detalle funcional.",
                contact = @Contact(name = "Victor Jimenez")
        ),
        security = @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth")
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        description = "Token JWT obtenido en POST /auth/login o POST /auth/signup. "
                + "Formato del header: Authorization: Bearer {token}"
)
public class OpenApiConfig {
}

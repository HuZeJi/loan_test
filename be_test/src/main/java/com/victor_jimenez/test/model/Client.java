package com.victor_jimenez.test.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

/**
 * Cliente del banco. Es titular de las solicitudes de prestamo (ver {@link Loan}).
 * Ver docs/DIAGRAMS.md - Diagrama ER.
 */
@Entity
@Table(name = "client")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Cliente del banco")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(description = "Identificador unico del cliente (UUID)", example = "b3f1c2a0-1234-4c56-9abc-1234567890ab")
    private String id;

    @NotBlank
    @Column(nullable = false)
    @Schema(description = "Nombre(s) del cliente", example = "Maria")
    private String name;

    @NotBlank
    @Column(name = "last_name", nullable = false)
    @Schema(description = "Apellido(s) del cliente", example = "Gonzalez")
    private String lastName;

    @NotNull
    @Past
    @Column(nullable = false)
    @Schema(description = "Fecha de nacimiento del cliente, debe ser una fecha pasada", example = "1990-05-20")
    private LocalDate birthday;

    @Schema(description = "Direccion fisica del cliente", example = "Ciudad de Guatemala")
    private String address;

    @NotBlank
    @Email
    @Column(nullable = false, unique = true)
    @Schema(description = "Correo electronico del cliente, unico en el sistema", example = "maria.gonzalez@example.com")
    private String email;

    @NotBlank
    @Column(name = "phone_number", nullable = false)
    @Schema(description = "Numero de telefono de contacto", example = "35157895")
    private String phoneNumber;

    @Column(name = "is_active", nullable = false)
    @Schema(description = "Indica si el cliente esta activo. Al eliminar un cliente se desactiva en lugar de borrarlo, y sus prestamos tambien se desactivan.", example = "true")
    private Boolean active = true;
}

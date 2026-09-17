package com.victor_jimenez.test.model;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Data
@Getter
@Setter
@Schema(description = "Datos de un cliente para crear, actualizar o consultar")
public class ClientDTO {

    @Schema(description = "Identificador unico del cliente (UUID). Se ignora al crear.", example = "b3f1c2a0-1234-4c56-9abc-1234567890ab")
    private String id;

    @NotBlank
    @Schema(description = "Nombre(s) del cliente", example = "Maria")
    private String name;

    @NotBlank
    @Schema(description = "Apellido(s) del cliente", example = "Gonzalez")
    private String lastName;

    @NotNull
    @Past
    @Schema(description = "Fecha de nacimiento, debe ser una fecha pasada", example = "1990-05-20")
    private LocalDate birthday;

    @Schema(description = "Direccion fisica del cliente", example = "Ciudad de guatemala")
    private String address;

    @NotBlank
    @Email
    @Schema(description = "Correo electronico del cliente, debe ser unico en el sistema", example = "maria.gonzalez@example.com")
    private String email;

    @NotBlank
    @Schema(description = "Numero de telefono de contacto", example = "35157895")
    private String phoneNumber;

    @Schema(description = "Indica si el cliente esta activo", example = "true")
    private boolean active;

    public Client toEntity(){
        Client client = new Client();
        client.setId(this.id);
        client.setName(this.name);
        client.setLastName(this.lastName);
        client.setBirthday(this.birthday);
        client.setAddress(this.address);
        client.setEmail(this.email);
        client.setPhoneNumber(this.phoneNumber);
        client.setActive(this.active);
        return client;
    }

    public static ClientDTO toDTO(Client client) {
        ClientDTO clientDto = new ClientDTO();
        clientDto.setId(client.getId());
        clientDto.setName(client.getName());
        clientDto.setLastName(client.getLastName());
        clientDto.setBirthday(client.getBirthday());
        clientDto.setAddress(client.getAddress());
        clientDto.setEmail(client.getEmail());
        clientDto.setPhoneNumber(client.getPhoneNumber());
        clientDto.setActive(client.getActive());
        return clientDto;
    }
}

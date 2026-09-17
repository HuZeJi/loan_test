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

import java.time.LocalDate;

@Data
@Getter
@Setter
public class ClientDTO {
    private String id;

    @NotBlank
    private String name;

    @NotBlank
    private String lastName;

    @NotNull
    @Past
    private LocalDate birthday;

    private String address;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String phoneNumber;
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

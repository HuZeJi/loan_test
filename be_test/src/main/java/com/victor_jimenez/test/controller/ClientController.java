package com.victor_jimenez.test.controller;

import com.victor_jimenez.test.model.ClientDTO;
import com.victor_jimenez.test.service.ClientSvc;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/client")
@Tag(name = "Clientes", description = "Alta, consulta, actualizacion y baja de clientes del banco")
public class ClientController {

    private final ClientSvc clientSvc;

    public ClientController(ClientSvc clientSvc) {
        this.clientSvc = clientSvc;
    }

    @PostMapping
    @Operation(
            summary = "Registrar un nuevo cliente",
            description = "Crea un cliente nuevo."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cliente creado exitosamente",
                    content = @Content(schema = @Schema(implementation = ClientDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos del cliente invalidos"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "409", description = "Ya existe un cliente con ese correo")
    })
    public ResponseEntity<ClientDTO> create(@Valid @RequestBody ClientDTO client) {
        ClientDTO saved = clientSvc.create(client);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Listar clientes", description = "Devuelve los clientes con paginacion.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado de clientes obtenido exitosamente"),
            @ApiResponse(responseCode = "401", description = "No autenticado")
    })
    public ResponseEntity<Page<ClientDTO>> list(Pageable pageable) {
        return ResponseEntity.ok(clientSvc.list(pageable));
    }

    @PutMapping("/{clientId}")
    @Operation(summary = "Actualizar un cliente", description = "Actualiza los datos personales de un cliente existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cliente actualizado exitosamente",
                    content = @Content(schema = @Schema(implementation = ClientDTO.class))),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    public ResponseEntity<ClientDTO> update(
            @Parameter(description = "Identificador del cliente (UUID)") @PathVariable String clientId,
            @RequestBody ClientDTO updates
    ) {
        ClientDTO saved = clientSvc.update(clientId, updates);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @DeleteMapping("/{clientId}")
    @Operation(
            summary = "Desactivar un cliente",
            description = "Da de baja logica al cliente (no lo elimina fisicamente) y desactiva en cascada sus prestamos."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente desactivado exitosamente"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "Identificador del cliente (UUID)") @PathVariable String clientId
    ) {
        clientSvc.delete(clientId);
        return ResponseEntity.ok().build();
    }
}

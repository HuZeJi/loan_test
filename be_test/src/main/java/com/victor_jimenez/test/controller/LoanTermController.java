package com.victor_jimenez.test.controller;

import com.victor_jimenez.test.model.LoanTermDTO;
import com.victor_jimenez.test.service.LoanTermSvc;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/loan-term")
@Tag(name = "Plazos de prestamo", description = "Consulta de los plazos disponibles para solicitar un prestamo")
public class LoanTermController {

    private final LoanTermSvc loanTermSvc;

    public LoanTermController(LoanTermSvc loanTermSvc) {
        this.loanTermSvc = loanTermSvc;
    }

    @GetMapping
    @Operation(summary = "Listar plazos disponibles", description = "Devuelve todos los plazos de prestamo configurados en el sistema.")
    public ResponseEntity<List<LoanTermDTO>> list() {
        return ResponseEntity.ok(loanTermSvc.list());
    }
}

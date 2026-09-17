package com.victor_jimenez.test.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Plazo disponible para una solicitud de prestamo")
public class LoanTermDTO {

    @Schema(description = "Identificador unico del plazo (UUID)", example = "d5b3e4c2-9012-4e78-9abc-1234567890ef")
    private String id;

    @Schema(description = "Descripcion del plazo", example = "12 meses")
    private String description;

    @Schema(description = "Duracion del plazo en dias", example = "360")
    private int days;

    public static LoanTermDTO toDTO(LoanTerm loanTerm) {
        LoanTermDTO dto = new LoanTermDTO();
        dto.setId(loanTerm.getId());
        dto.setDescription(loanTerm.getDescription());
        dto.setDays(loanTerm.getDays());
        return dto;
    }
}

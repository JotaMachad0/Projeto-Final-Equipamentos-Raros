package br.com.raroacademy.demo.domain.DTO.expectedReturn;

import br.com.raroacademy.demo.domain.DTO.equipmentCollaborator.EquipmentCollaboratorResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record ExpectedReturnResponseDTO (

    @Schema(description = "Unique ID of the expected return record", example = "1")
    Long id,

    @Schema(description = "Expected date for the equipment return", example = "2026-07-31")
    LocalDate expectedReturnDate,

    @Schema(description = "Data of the loan associated with this expected return")
    EquipmentCollaboratorResponseDTO equipmentCollaborator) {

}
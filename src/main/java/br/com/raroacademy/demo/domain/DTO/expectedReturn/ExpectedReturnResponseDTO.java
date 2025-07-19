package br.com.raroacademy.demo.domain.DTO.expectedReturn;

import br.com.raroacademy.demo.domain.DTO.equipmentCollaborator.EquipmentCollaboratorResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class ExpectedReturnResponseDTO {

    @Schema(description = "Unique ID of the expected return record", example = "1")
    private Long id;

    @Schema(description = "Expected date for the equipment return", example = "2026-07-31")
    private LocalDate expectedReturnDate;

    @Schema(description = "Data of the loan associated with this expected return")
    private EquipmentCollaboratorResponseDTO equipmentCollaborator;

}
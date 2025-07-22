package br.com.raroacademy.demo.domain.DTO.equipment.returns;

import br.com.raroacademy.demo.domain.DTO.equipment.collaborator.EquipmentCollaboratorResponseDTO;
import br.com.raroacademy.demo.domain.enums.ReturnStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record EquipmentReturnResponseDTO(
        @Schema(description = "Unique ID of the equipment return record", example = "1")
        Long id,

        @Schema(description = "Expected date for the equipment return", example = "2026-07-31")
        LocalDate deliveryDate,

        @Schema(description = "Actual date the equipment was received", example = "2026-07-30")
        LocalDate receiptDate,

        @Schema(description = "Status of the return process", example = "PENDING")
        ReturnStatus status,

        @Schema(description = "Additional notes about the return", example = "Returned with minor scratches")
        String note,

        @Schema(description = "Data of the loan associated with this return")
        EquipmentCollaboratorResponseDTO equipmentCollaborator
) {
}
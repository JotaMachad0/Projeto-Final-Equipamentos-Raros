package br.com.raroacademy.demo.domain.DTO.equipment.collaborator;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record NewCollaboratorEquipmentLinkRequestDTO(

        @NotNull
        @Schema(description = "ID of the collaborator to be linked.", example = "1")
        Long collaboratorId,

        @NotNull
        @Schema(description = "ID of the equipment to be linked.", example = "1")
        Long equipmentId,

        @NotNull
        @Schema(description = "Date the equipment was shipped.", example = "2024-07-18")
        LocalDate shipmentDate,

        @Schema(description = "Shipment status.", example = "SHIPPED")
        String shipmentStatus,

        @Schema(description = "Additional notes about the shipment.", example = "Equipment shipped with all accessories.")
        String notes

) {
}
package br.com.raroacademy.demo.domain.DTO.equipmentCollaborator;

import br.com.raroacademy.demo.domain.DTO.collaborator.CollaboratorSummaryDTO;
import br.com.raroacademy.demo.domain.DTO.equipment.EquipmentSummaryDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class EquipmentCollaboratorResponseDTO {

    @Schema(example = "1")
    private Long id;

    @Schema(description = "Date the equipment was delivered", example = "2025-07-17")
    private LocalDate deliveryDate;

    @Schema(description = "Date the equipment was returned", example = "2026-07-16")
    private LocalDate returnDate;

    @Schema(description = "Current delivery status", example = "Delivered to collaborator")
    private String deliveryStatus;

    @Schema(description = "General notes about the delivery or return", example = "Equipment in perfect condition, with charger and mouse.")
    private String notes;

    @Schema(description = "Data of the collaborator who received the equipment")
    private CollaboratorSummaryDTO collaborator;

    @Schema(description = "Data of the loaned equipment")
    private EquipmentSummaryDTO equipment;
}
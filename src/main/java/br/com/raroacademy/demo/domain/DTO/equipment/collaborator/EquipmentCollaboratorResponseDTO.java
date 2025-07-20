package br.com.raroacademy.demo.domain.DTO.equipment.collaborator;

import br.com.raroacademy.demo.domain.DTO.collaborator.CollaboratorSummaryDTO;
import br.com.raroacademy.demo.domain.DTO.equipment.EquipmentSummaryDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record EquipmentCollaboratorResponseDTO (

    @Schema(example = "1")
    Long id,

    @Schema(description = "Date the equipment was delivered", example = "2025-07-17")
    LocalDate deliveryDate,

    @Schema(description = "Forecast for when the equipment will be delivered based on collaborator's region", example = "2025-07-22")
    @JsonProperty("prevision_delivery_date")
    LocalDate previsionDeliveryDate,

    @Schema(description = "Date the equipment was returned", example = "2026-07-16")
    LocalDate returnDate,

    @Schema(description = "Current delivery status", example = "Delivered to collaborator")
    String deliveryStatus,

    @Schema(description = "General notes about the delivery or return", example = "Equipment in perfect condition, with charger and mouse.")
    String notes,

    @Schema(description = "Data of the collaborator who received the equipment")
    CollaboratorSummaryDTO collaborator,

    @Schema(description = "Data of the loaned equipment")
    EquipmentSummaryDTO equipment) {
}
package br.com.raroacademy.demo.domain.DTO.equipment.collaborator;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record EquipmentCollaboratorRequestDTO (

    @NotNull
    @Schema(example = "1")
    Long collaboratorId,

    @NotNull
    @Schema(example = "1")
    Long equipmentId,

    @NotNull
    @Schema(example = "2024-07-18")
    LocalDate deliveryDate,

    @Schema(example = "2025-07-17")
    LocalDate returnDate,

    @Schema(example = "Delivered")
    String deliveryStatus,

    @Schema(example = "All accessories have been delivered")
    String notes) {
}
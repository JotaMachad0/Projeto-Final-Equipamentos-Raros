package br.com.raroacademy.demo.domain.DTO.equipment.collaborator;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class EquipmentCollaboratorRequestDTO {

    @NotNull
    @Schema(example = "1")
    private Long collaboratorId;

    @NotNull
    @Schema(example = "1")
    private Long equipmentId;

    @NotNull
    @Schema(example = "2024-07-18")
    private LocalDate deliveryDate;

    @Schema(example = "2025-07-17")
    private LocalDate returnDate;

    @Schema(example = "Delivered")
    private String deliveryStatus;

    @Schema(example = "All accessories have been delivered")
    private String notes;
}
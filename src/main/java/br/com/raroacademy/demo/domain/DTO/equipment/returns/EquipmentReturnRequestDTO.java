package br.com.raroacademy.demo.domain.DTO.equipment.returns;

import br.com.raroacademy.demo.domain.enums.ReturnStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

public record EquipmentReturnRequestDTO(

        @NotNull
        @Schema(description = "Date on which the equipment is delivered.", example = "2025-08-31")
        LocalDate deliveryDate,

        @NotNull
        @Schema(description = "ID of the equipment allocation to be returned.", example = "1")
        Long equipmentCollaboratorId,

        @PastOrPresent(message = "The receipt date cannot be in the future.")
        @Schema(description = "Date the company received the equipment. Used when processing the return.", example = "")
        LocalDate receiptDate,

        @Schema(description = "Status of the return. Used when updating the state.", example = "")
        ReturnStatus status,

        @Schema(description = "Notes about the return.", example = "")
        String note
) {
}
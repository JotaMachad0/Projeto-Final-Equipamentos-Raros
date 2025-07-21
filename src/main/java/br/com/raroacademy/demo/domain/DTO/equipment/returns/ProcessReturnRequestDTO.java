package br.com.raroacademy.demo.domain.DTO.equipment.returns;

import br.com.raroacademy.demo.domain.enums.EquipmentStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import java.time.LocalDate;

public record ProcessReturnRequestDTO(
        @NotNull
        @PastOrPresent(message = "The receipt date cannot be in the future.")
        LocalDate receiptDate,

        @NotNull(message = "You must provide the current status of the equipment.")
        EquipmentStatus equipmentCurrentStatus,

        String note
) {}
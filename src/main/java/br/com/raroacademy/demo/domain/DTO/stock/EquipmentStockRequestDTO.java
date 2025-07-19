package br.com.raroacademy.demo.domain.DTO.stock;

import br.com.raroacademy.demo.domain.enums.EquipmentType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Data required for creating or updating an equipment type.")
public record EquipmentStockRequestDTO(
        @NotNull
        @Schema(example = "NOTEBOOK", description = "Equipment type.")
        EquipmentType equipmentType,

        @NotNull
        @NotNull(message = "The current stock is required.")
        @Min(value = 0, message = "The current stock can't be negative.")
        Integer currentStock) {
}

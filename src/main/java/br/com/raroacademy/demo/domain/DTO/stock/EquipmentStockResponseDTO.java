package br.com.raroacademy.demo.domain.DTO.stock;

import br.com.raroacademy.demo.domain.enums.EquipmentType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "Data returned about the stock of an equipment type.")
public record EquipmentStockResponseDTO(
        @Schema(example = "1", description = "ID of the equipment type in stock.")
        Long id,

        @Schema(example = "NOTEBOOK", description = "Equipment type.")
        EquipmentType equipmentType,

        @Schema(example = "25", description = "Current stock.")
        Integer currentStock) {
}

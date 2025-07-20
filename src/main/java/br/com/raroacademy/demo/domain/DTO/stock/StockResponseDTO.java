package br.com.raroacademy.demo.domain.DTO.stock;

import br.com.raroacademy.demo.domain.enums.EquipmentType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(name = "StockResponseDTO", description = "Response DTO with stock parameter information")
public record StockResponseDTO (

    @Schema(description = "Parameter ID", example = "1")
    Long id,

    @Schema
    EquipmentType equipmentType,

    @Schema(description = "Recommended minimum stock", example = "10")
    Integer minStock,

    @Schema(description = "Security stock", example = "15")
    Integer securityStock,

    @Schema(description = "Current stock", example = "0")
    Integer currentStock,

    @Schema(description = "Average restock time (in days)", example = "7")
    Integer avgRestockTimeDays,

    @Schema(description = "Average stock consumption time (in days)", example = "15")
    Integer avgStockConsumptionTimeDays,

    @Schema(description = "Average defective equipment rate", example = "0.05")
    Float avgDefectiveRate) {
}

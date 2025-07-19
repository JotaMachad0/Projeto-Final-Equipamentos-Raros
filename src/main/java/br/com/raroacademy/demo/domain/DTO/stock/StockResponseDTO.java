package br.com.raroacademy.demo.domain.DTO.stock;

import br.com.raroacademy.demo.domain.DTO.equipment.EquipmentSummaryDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(name = "StockResponseDTO", description = "Response DTO with stock parameter information")
public class StockResponseDTO {

    @Schema(description = "Parameter ID", example = "1")
    private Long id;

    @Schema(description = "Recommended minimum stock", example = "10")
    private Integer minStock;

    @Schema(description = "Security stock", example = "15")
    private Integer securityStock;

    @Schema(description = "Current stock", example = "0")
    private Integer currentStock;

    @Schema(description = "Average restock time (in days)", example = "7")
    private Integer avgRestockTimeDays;

    @Schema(description = "Average stock consumption time (in days)", example = "15")
    private Integer avgStockConsumptionTimeDays;

    @Schema(description = "Average defective equipment rate", example = "0.05")
    private Float avgDefectiveRate;

}

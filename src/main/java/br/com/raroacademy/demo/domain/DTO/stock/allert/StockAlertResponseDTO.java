package br.com.raroacademy.demo.domain.DTO.stock.allert;

import br.com.raroacademy.demo.domain.enums.EquipmentType;
import br.com.raroacademy.demo.domain.enums.StockAlertStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.sql.Timestamp;

@Builder
@Schema(description = "Data returned about the stock alert.")
public record StockAlertResponseDTO(
        @Schema(example = "1", description = "Stock alert ID.")
        Long id,

        @Schema(example = "NOTEBOOK", description = "Equipment type.")
        EquipmentType equipmentType,

        @Schema(example = "15", description = "Current stock.")
        Integer currentStock,

        @Schema(example = "5", description = "Minimum stock.")
        Integer minimumStock,

        @Schema(example = "10", description = "Security stock.")
        Integer securityStock,

        @Schema(example = "2025-12-31T12:30:00", description = "Time and date the alert was sent.")
        Timestamp alertSentAt,

        @Schema(example = "PROCESSADO", description = "Stock alert status.")
        StockAlertStatus stockAlertStatus) {
}

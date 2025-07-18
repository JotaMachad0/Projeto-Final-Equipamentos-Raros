package br.com.raroacademy.demo.domain.DTO.stock.allert;

import br.com.raroacademy.demo.domain.entities.EquipmentType;
import br.com.raroacademy.demo.domain.entities.StockAlertStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.sql.Timestamp;

@Builder
@Schema(description = "Dados retornados sobre o alerta de estoque")
public record StockAlertResponseDTO(
        @Schema(example = "1", description = "ID do alerta de estoque")
        Long id,

        @Schema(example = "Notebook", description = "Tipo do equipamento")
        EquipmentType equipmentType,

        @Schema(example = "15", description = "Estoque atual")
        Integer currentStock,

        @Schema(example = "5", description = "Estoque mínimo")
        Integer minimumStock,

        @Schema(example = "10", description = "Estoque de segurança")
        Integer securityStock,

        @Schema(example = "2025-12-31T12:30:00", description = "Data e hora de envio do alerta")
        Timestamp alertSentAt,

        @Schema(example = "Recebido", description = "Status do alerta de esotque")
        StockAlertStatus stockAlertStatus) {
}

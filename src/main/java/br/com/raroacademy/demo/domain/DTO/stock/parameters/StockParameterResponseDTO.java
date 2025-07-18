package br.com.raroacademy.demo.domain.DTO.stock.parameters;

import br.com.raroacademy.demo.domain.DTO.equipment.EquipmentSummaryDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(name = "StockParameterResponseDTO", description = "DTO de resposta com informações dos parâmetros de estoque")
public class StockParameterResponseDTO {

    @Schema(description = "ID do parâmetro", example = "1")
    private Long id;

    @Schema(description = "Estoque mínimo recomendado", example = "10")
    private Integer minStock;

    @Schema(description = "Tempo médio de reposição (em dias)", example = "7")
    private Integer avgRestockTimeDays;

    @Schema(description = "Tempo médio de consumo do estoque (em dias)", example = "15")
    private Integer avgStockConsumptionTimeDays;

    @Schema(description = "Taxa média de equipamentos com defeito", example = "0.05")
    private Float avgDefectiveRate;

    @Schema(description = "Informações do equipamento vinculado")
    private EquipmentSummaryDTO equipment;
}

package br.com.raroacademy.demo.domain.DTO.stock.parameters;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(name = "StockParameterRequestDTO", description = "DTO para criação de parâmetros de estoque")
public class StockParameterRequestDTO {

    @NotBlank
    @Schema(description = "Tipo de equipamento", example = "Notebook")
    private String equipmentType;

    @NotNull
    @Schema(description = "Estoque mínimo recomendado", example = "10")
    private Integer minStock;

    @NotNull
    @Schema(description = "Tempo médio de reposição (em dias)", example = "7")
    private Integer avgRestockTimeDays;

    @NotNull
    @Schema(description = "Tempo médio de consumo do estoque (em dias)", example = "15")
    private Integer avgStockConsumptionTimeDays;

    @NotNull
    @Schema(description = "Tempo médio de entrega (em dias)", example = "3")
    private Integer avgDeliveryTimeDays;

    @NotNull
    @Schema(description = "Taxa média de equipamentos com defeito", example = "0.05")
    private Float avgDefectiveRate;
}

package br.com.raroacademy.demo.domain.DTO.equipment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(name = "EquipmentSummaryDTO", description = "Resumo das informações do equipamento")
public record EquipmentSummaryDTO (

    @Schema(description = "Equipment ID", example = "4")
    Long id,

    @Schema(description = "Type of equipment", example = "Notebook")
    String type,

    @Schema(description = "Equipment model", example = "XPS 13")
    String model) {
}

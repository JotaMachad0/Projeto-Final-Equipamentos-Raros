package br.com.raroacademy.demo.domain.DTO.equipment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(name = "EquipmentSummaryDTO", description = "Resumo das informações do equipamento")
public class EquipmentSummaryDTO {

    @Schema(description = "ID do equipamento", example = "1")
    private Long id;

    @Schema(description = "Tipo do equipamento", example = "NOTEBOOK")
    private String type;

    @Schema(description = "Modelo do equipamento", example = "XPS 13")
    private String model;

}

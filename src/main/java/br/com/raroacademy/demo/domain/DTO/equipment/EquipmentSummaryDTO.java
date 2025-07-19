package br.com.raroacademy.demo.domain.DTO.equipment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(name = "EquipmentSummaryDTO", description = "Resumo das informações do equipamento")
public class EquipmentSummaryDTO {

    @Schema(description = "Equipment ID", example = "4")
    private Long id;

    @Schema(description = "Type of equipment", example = "Notebook")
    private String type;

    @Schema(description = "Equipment model", example = "XPS 13")
    private String model;


}

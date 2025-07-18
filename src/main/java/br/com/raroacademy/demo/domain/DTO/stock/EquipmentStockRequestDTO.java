package br.com.raroacademy.demo.domain.DTO.stock;

import br.com.raroacademy.demo.domain.entities.EquipmentType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Dados para cadastro ou atualização do estoque de um tipo de equipamento")
public record EquipmentStockRequestDTO(
        @NotNull
        @Schema(example = "Notebook", description = "Tipo do equipamento")
        EquipmentType equipmentType,

        @NotNull
        @Min(value = 0, message = "A quantidade não pode ser negativa")
        @Schema(example = "20", description = "Quantidade disponível em estoque")
        Integer quantity) {
}

package br.com.raroacademy.demo.domain.DTO.stock;


import br.com.raroacademy.demo.domain.entities.EquipmentType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "Dados retornados sobre o estoque de um tipo de equipamento")
public record EquipmentStockResponseDTO(
        @Schema(example = "1", description = "ID do registro de estoque")
        Long id,

        @Schema(example = "Notebook", description = "Tipo do equipamento")
        EquipmentType equipmentType,

        @Schema(example = "25", description = "Quantidade disponível em estoque")
        Integer quantity) {
}

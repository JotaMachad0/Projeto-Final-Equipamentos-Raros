package br.com.raroacademy.demo.domain.DTO.equipment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDate;

@Builder
@Schema(name = "EquipmentResponseDTO", description = "DTO de resposta com informações do equipamento")
public record EquipmentResponseDTO (

    @Schema(description = "ID do equipamento", example = "1")
    Long id,

    @Schema(description = "Tipo do equipamento", example = "Notebook")
    String type,

    @Schema(description = "Número de série único", example = "SN-123456789")
    String serialNumber,

    @Schema(description = "Marca do equipamento", example = "Dell")
    String brand,

    @Schema(description = "Modelo do equipamento", example = "Inspiron 15 3000")
    String model,

    @Schema(description = "Especificações adicionais", example = "8GB RAM, SSD 256GB")
    String specs,

    @Schema(description = "Data de aquisição", example = "2024-01-15")
    LocalDate acquisitionDate,

    @Schema(description = "Tempo de uso em meses", example = "12")
    Integer usageTimeMonths,

    @Schema(description = "Status atual do equipamento", example = "EM_USO")
    String status) {
}

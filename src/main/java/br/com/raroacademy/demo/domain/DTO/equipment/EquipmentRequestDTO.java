package br.com.raroacademy.demo.domain.DTO.equipment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

@Schema(name = "EquipmentRequestDTO", description = "DTO para criação de equipamento")
public record EquipmentRequestDTO (

    @NotBlank
    @Schema(description = "Tipo do equipamento", example = "Notebook")
    String type,

    @NotBlank
    @Schema(description = "Número de série único", example = "SN-123456789")
    String serialNumber,

    @NotBlank
    @Schema(description = "Marca do equipamento", example = "Dell")
    String brand,

    @NotBlank
    @Schema(description = "Modelo do equipamento", example = "Inspiron 15 3000")
    String model,

    @Schema(description = "Especificações adicionais", example = "8GB RAM, SSD 256GB")
    String specs,

    @NotNull
    @Schema(description = "Data de aquisição", example = "2024-01-15")
    LocalDate acquisitionDate,

    @NotNull
    @Schema(description = "Tempo de uso em meses", example = "12")
    Integer usageTimeMonths,

    @NotBlank
    @Schema(description = "Status atual do equipamento", example = "EM_USO")
    String status) {
}

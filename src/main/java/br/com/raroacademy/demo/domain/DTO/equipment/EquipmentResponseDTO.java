package br.com.raroacademy.demo.domain.DTO.equipment;

import br.com.raroacademy.demo.domain.enums.EquipmentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDate;

@Builder
@Schema(name = "EquipmentResponseDTO", description = "DTO de resposta com informações do equipamento")
public record EquipmentResponseDTO (

    @Schema(description = "Equipment ID", example = "1")
    Long id,

    @Schema(description = "Equipment type", example = "NOTEBOOK_CHARGER")
    String type,

    @Schema(description = "Unique serial number", example = "SN-123456789")
    String serialNumber,

    @Schema(description = "Equipment brand", example = "Dell")
    String brand,

    @Schema(description = "Equipment model", example = "Inspiron 15 3000")
    String model,

    @Schema(description = "Additional specifications", example = "8GB RAM, SSD 256GB")
    String specs,

    @Schema(description = "Acquisition date", example = "2024-01-15")
    LocalDate acquisitionDate,

    @Schema(description = "Usage time in months", example = "12")
    Integer usageTimeMonths,

    @Schema(description = "Current status of the equipment", example = "IN_USE")
    EquipmentStatus status) {
}

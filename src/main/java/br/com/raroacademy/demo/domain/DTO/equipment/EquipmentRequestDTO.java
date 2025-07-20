package br.com.raroacademy.demo.domain.DTO.equipment;

import br.com.raroacademy.demo.domain.enums.EquipmentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

@Schema(name = "EquipmentRequestDTO", description = "DTO para criação de equipamento")
public record EquipmentRequestDTO (
    @NotBlank
    @Schema(description = "Equipment type", example = "NOTEBOOK_CHARGER")
    String type,

    @NotBlank
    @Schema(description = "Unique serial number", example = "SN-123456789")
    String serialNumber,

    @NotBlank
    @Schema(description = "Equipment brand", example = "Dell")
    String brand,

    @NotBlank
    @Schema(description = "Equipment model", example = "Inspiron 15 3000")
    String model,

    @Schema(description = "Additional specifications", example = "8GB RAM, SSD 256GB")
    String specs,

    @NotNull
    @Schema(description = "Acquisition date", example = "2024-01-15")
    LocalDate acquisitionDate,

    @NotNull
    @Schema(description = "Usage time in months", example = "12")
    Integer usageTimeMonths,

    @Schema(description = "Current status of the equipment", example = "IN_USE")
    EquipmentStatus status) {
}

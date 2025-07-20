package br.com.raroacademy.demo.domain.DTO.stock;

import br.com.raroacademy.demo.domain.enums.EquipmentType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Schema(name = "StockRequestDTO", description = "Data required for updating a stock.")
public record StockRequestDTO (
    @Min(value = 0)
    @Schema(description = "Current stock")
    Integer currentStock,

    @Min(value = 0)
    @Schema(description = "Minimum stock")
    Integer minStock,

    @Schema(description = "Security stock")
    Integer securityStock) {
}
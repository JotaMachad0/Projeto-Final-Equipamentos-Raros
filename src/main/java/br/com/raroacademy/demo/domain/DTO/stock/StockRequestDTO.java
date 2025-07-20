package br.com.raroacademy.demo.domain.DTO.stock;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(name = "StockRequestDTO", description = "Data required for updating a stock.")
public record StockRequestDTO (
    @Min(value = 0)
    @Schema(description = "Minimum stock")
    Integer minStock,

    @Schema(description = "Security stock")
    Integer securityStock) {
}
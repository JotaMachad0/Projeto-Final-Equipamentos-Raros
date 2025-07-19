package br.com.raroacademy.demo.domain.DTO.stock;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(name = "StockRequestDTO", description = "DTO for creating stock parameters")
public class StockRequestDTO {

    @NotNull
    @Schema(description = "Equipment ID", example = "1")
    private Long equipmentId;

    @NotNull
    @Schema(description = "Average restock time (in days)", example = "7")
    private Integer avgRestockTimeDays;

    @NotNull
    @Schema(description = "Average stock consumption time (in days)", example = "15")
    private Integer avgStockConsumptionTimeDays;
}

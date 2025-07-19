package br.com.raroacademy.demo.domain.DTO.stock;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(name = "UpdateStockRequestDTO", description = "DTO for updating stock parameters")
public class UpdateStockRequestDTO {

    @NotNull
    @Schema(description = "Minimum stock", example = "10")
    private Integer minStock;

    @NotNull
    @Schema(description = "Security stock", example = "15")
    private Integer securityStock;

    @NotNull
    @Schema(description = "Average restock time in days", example = "7")
    private Integer avgRestockTimeDays;

    @NotNull
    @Schema(description = "Average stock consumption time in days", example = "15")
    private Integer avgStockConsumptionTimeDays;
}

package br.com.raroacademy.demo.domain.DTO.equipment.purchase;

import br.com.raroacademy.demo.domain.enums.PurchaseStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record EquipmentPurchasesRequestDTO(
        @NotBlank
        @Schema(example = "Laptop")
        String equipmentType,

        @NotNull
        @Positive
        @Schema(example = "10")
        Integer quantity,

        @NotNull
        @Schema(example = "2025-07-19")
        LocalDate orderDate,

        @NotBlank
        @Schema(example = "Tech Supplies Inc.")
        String supplier,

        @Schema(description = "Receipt Date.", example = "")
        LocalDate receiptDate,
        
        @Schema(description = "Purchase status.", example = "purchased")
        PurchaseStatus status
) {
}
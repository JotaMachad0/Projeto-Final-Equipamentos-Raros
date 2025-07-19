package br.com.raroacademy.demo.domain.DTO.equipmentPurchase;

import br.com.raroacademy.demo.domain.enums.PurchaseStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record EquipmentPurchasesResponseDTO(
        @Schema(example = "1")
        Long id,

        @Schema(example = "Laptop")
        String equipmentType,

        @Schema(example = "10")
        Integer quantity,

        @Schema(example = "2025-07-19")
        LocalDate orderDate,

        @Schema(example = "Tech Supplies Inc.")
        String supplier,

        @Schema(description = "Receipt Date.", example = "2025-07-29")
        LocalDate receiptDate,
        
        @Schema(description = "Purchase status.", example = "purchased")
        PurchaseStatus status
) {
}
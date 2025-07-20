package br.com.raroacademy.demo.domain.DTO.expected.hirings;

import br.com.raroacademy.demo.domain.enums.ExpectedHiringStatus;
import br.com.raroacademy.demo.domain.enums.Region;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDate;

@Builder
@Schema(description = "Data returned about an expected hiring.")
public record ExpectedHiringResponseDTO(
        @Schema(example = "1", description = "Expected hiring ID.")
        Long id,

        @Schema(example = "2025-12-31", description = "Expected date.")
        LocalDate expectedHireDate,

        @Schema(example = "Developer", description = "Expected position.")
        String position,

        @Schema(example = "2 cellphones, 1 16GB notebook.",
                description = "Equipment requirements.")
        String equipmentRequirements,

        @Schema(example = "CENTRO_OESTE", description = "Region of origin.")
        Region region,

        @Schema(example = "PROCESSED", description = "Expected hiring status.")
        ExpectedHiringStatus expectedHiringStatus) {
}

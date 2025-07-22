package br.com.raroacademy.demo.domain.DTO.expected.hirings;

import br.com.raroacademy.demo.domain.enums.Region;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

@Schema(description = "Data required for creating or updating an expected hiring.")
public record ExpectedHiringRequestDTO(
        @NotNull
        @Future(message = "The hiring date must be in the future.")
        @Schema(example = "2025-12-31", description = "Expected date.")
        LocalDate expectedHireDate,

        @NotBlank
        @Length(max = 100)
        @Schema(example = "Developer", description = "Expected position.")
        String position,

        @NotBlank
        @Schema(example = "2 cellphones, 1 16GB notebook.",
                description = "Equipment requirements.")
        String equipmentRequirements,

        @NotNull
        @Schema(example = "CENTRO_OESTE", description = "Region of origin.")
        Region region) {
}

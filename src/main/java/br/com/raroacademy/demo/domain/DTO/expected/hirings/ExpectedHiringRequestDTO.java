package br.com.raroacademy.demo.domain.DTO.expected.hirings;

import br.com.raroacademy.demo.domain.enums.Region;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

@Schema(description = "Expected hiring data provided by the user")
public record ExpectedHiringRequestDTO(
        @NotNull
        @Future(message = "The hiring date must be in the future")
        @Schema(example = "2025-12-31", description = "Expected date for the hiring(s)")
        LocalDate expectedHireDate,

        @NotBlank
        @Length(max = 100)
        @Schema(example = "Developer", description = "Position expected for the hiring(s)")
        String position,

        @NotBlank
        @Schema(example = "1 laptop 16GB, 2 smartphones",
                description = "Equipment requirements for the expected hiring(s)")
        String equipmentRequirements,

        @NotNull
        @Schema(example = "Midwest", description = "Region of origin of the person(s) to be hired")
        Region region) {
}

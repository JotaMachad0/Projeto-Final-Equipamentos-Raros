package br.com.raroacademy.demo.domain.DTO.expected.hirings;

import br.com.raroacademy.demo.domain.enums.Region;
import br.com.raroacademy.demo.domain.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDate;

@Builder
@Schema(description = "Expected hiring data returned by the API")
public record ExpectedHiringResponseDTO(
        @Schema(example = "1", description = "Expected hiring ID")
        Long id,

        @Schema(example = "2025-12-31", description = "Expected date for the hiring(s)")
        LocalDate expectedHireDate,

        @Schema(example = "Developer", description = "Position expected for the hiring(s)")
        String position,

        @Schema(example = "1 laptop 16GB, 2 smartphones",
                description = "Equipment requirements for the expected hiring(s)")
        String equipmentRequirements,

        @Schema(example = "Midwest", description = "Region of origin of the person(s) to be hired")
        Region region,

        @Schema(example = "Processed", description = "Status of the expected hiring")
        Status status) {
}

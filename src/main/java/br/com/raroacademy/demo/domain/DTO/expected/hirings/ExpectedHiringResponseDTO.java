package br.com.raroacademy.demo.domain.DTO.expected.hirings;

import br.com.raroacademy.demo.domain.entities.ExpectedHiringStatus;
import br.com.raroacademy.demo.domain.entities.Region;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDate;

@Builder
@Schema(description = "Dados retornados sobre uma previsão de contratação")
public record ExpectedHiringResponseDTO(
        @Schema(example = "1", description = "ID da previsão de contratação")
        Long id,

        @Schema(example = "2025-12-31", description = "Data prevista")
        LocalDate expectedHireDate,

        @Schema(example = "Desenvolvedor", description = "Cargo previsto")
        String position,

        @Schema(example = "1 notebook 16GB, 2 celulares",
                description = "Equipamentos requisitados")
        String equipmentRequirements,

        @Schema(example = "Centro-Oeste", description = "Região de origem")
        Region region,

        @Schema(example = "Processada", description = "Status da previsão de contratação")
        ExpectedHiringStatus expectedHiringStatus) {
}

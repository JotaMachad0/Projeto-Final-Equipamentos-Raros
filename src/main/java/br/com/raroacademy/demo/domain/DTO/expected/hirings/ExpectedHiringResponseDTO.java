package br.com.raroacademy.demo.domain.DTO.expected.hirings;

import br.com.raroacademy.demo.domain.enums.Region;
import br.com.raroacademy.demo.domain.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDate;

@Builder
@Schema(description = "Dados da previsão de contratação retornados pela API")
public record ExpectedHiringResponseDTO(
        @Schema(example = "1", description = "ID da previsão de contratação")
        Long id,

        @Schema(example = "2025-12-31", description = "Data prevista para a(s) contratação(ões)")
        LocalDate expectedHireDate,

        @Schema(example = "Desenvolvedor", description = "Cargo previsto para a(s) contratação(ões)")
        String position,

        @Schema(example = "1 notebook 16GB, 2 celulares",
                description = "Equipamentos requisitados pela previsão de contratação")
        String equipmentRequirements,

        @Schema(example = "Centro-Oeste", description = "Região de origem da(s) pessoa(s) a ser(em) contratada(s)")
        Region region,

        @Schema(example = "Processada", description = "Status da previsão de contratação")
        Status status) {
}

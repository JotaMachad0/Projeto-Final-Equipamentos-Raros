package br.com.raroacademy.demo.domain.DTO.expected.hirings;

import br.com.raroacademy.demo.domain.entities.Region;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

@Schema(description = "Dados da previsão de contratação fornecidos pelo usuário")
public record ExpectedHiringRequestDTO(
        @NotNull
        @Future(message = "A data de contratação deve ser no futuro")
        @Schema(example = "2025-12-31", description = "Data prevista para a(s) contratação(ões)")
        LocalDate expectedHireDate,

        @NotBlank
        @Length(max = 100)
        @Schema(example = "Desenvolvedor", description = "Cargo previsto para a(s) contratação(ões)")
        String position,

        @NotBlank
        @Schema(example = "1 notebook 16GB, 2 celulares",
                description = "Equipamentos requisitados pela previsão de contratação")
        String equipmentRequirements,

        @NotNull
        @Schema(example = "Centro-Oeste", description = "Região de origem da(s) pessoa(s) a ser(em) contratada(s)")
        Region region) {
}

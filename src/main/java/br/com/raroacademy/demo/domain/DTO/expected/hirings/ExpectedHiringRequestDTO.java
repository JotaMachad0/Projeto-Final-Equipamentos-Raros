package br.com.raroacademy.demo.domain.DTO.expected.hirings;

import br.com.raroacademy.demo.domain.entities.Region;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

public record ExpectedHiringRequestDTO(
        @NotNull
        @Future(message = "A data de contratação deve ser no futuro")
        LocalDate expectedHireDate,

        @NotBlank
        @Length(max = 100)
        String position,

        @NotBlank
        String equipmentRequirements,

        @NotNull
        Region region) {
}

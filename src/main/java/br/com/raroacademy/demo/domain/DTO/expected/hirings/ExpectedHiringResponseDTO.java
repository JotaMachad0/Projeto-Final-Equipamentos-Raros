package br.com.raroacademy.demo.domain.DTO.expected.hirings;

import br.com.raroacademy.demo.domain.entities.Region;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record ExpectedHiringResponseDTO(
        Long id,
        LocalDate expectedHireDate,
        String position,
        String equipmentRequirements,
        Region region) {
}

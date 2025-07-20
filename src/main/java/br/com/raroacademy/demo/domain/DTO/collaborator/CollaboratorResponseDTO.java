package br.com.raroacademy.demo.domain.DTO.collaborator;

import br.com.raroacademy.demo.domain.DTO.address.AddressResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record CollaboratorResponseDTO(
        @Schema(example = "1")
        Long id,

        @Schema(example = "Collaborator's name")
        String name,

        @Schema(example = "123.456.789-00")
        String cpf,

        @Schema(example = "collaborator@email.com")
        String email,

        @Schema(example = "31999998888")
        String phone,

        @Schema(example = "2024-01-15")
        LocalDate contractStartDate,

        @Schema(example = "2025-01-14")
        LocalDate contractEndDate,

        AddressResponseDTO address
) {
}
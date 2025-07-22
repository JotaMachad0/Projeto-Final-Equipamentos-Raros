package br.com.raroacademy.demo.domain.DTO.collaborator;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record CollaboratorSummaryDTO(
        @Schema(description = "Collaborator ID", example = "1")
        Long id,

        @Schema(description = "Collaborator's name", example = "Collaborator Name")
        String name,

        @Schema(description = "Collaborator's CPF", example = "433.996.789-00")
        String cpf,

        @Schema(description = "Collaborator's email", example = "colaborador2@exemplo.com")
        String email
) {
}
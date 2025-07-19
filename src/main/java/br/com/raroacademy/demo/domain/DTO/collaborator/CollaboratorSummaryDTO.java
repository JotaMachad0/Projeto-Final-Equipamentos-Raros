package br.com.raroacademy.demo.domain.DTO.collaborator;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CollaboratorSummaryDTO {

    @Schema(description = "Collaborator ID", example = "1")
    private Long id;

    @Schema(description = "Collaborator's name", example = "Collaborator Name")
    private String name;

    @Schema(description = "Collaborator's CPF", example = "433.996.789-00")
    private String cpf;

    @Schema(description = "Collaborator's email", example = "colaborador2@exemplo.com")
    private String email;
}
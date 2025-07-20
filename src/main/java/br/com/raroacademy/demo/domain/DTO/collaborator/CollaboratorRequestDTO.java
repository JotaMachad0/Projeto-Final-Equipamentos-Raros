package br.com.raroacademy.demo.domain.DTO.collaborator;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Builder
public record CollaboratorRequestDTO(
        @NotBlank(message = "Nome é obrigatório")
        @Size(max = 255, message = "Nome deve ter no máximo 255 caracteres")
        @Schema(example = "Collaborator name")
        String name,

        @NotBlank(message = "CPF é obrigatório")
        @Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}|\\d{11}", message = "CPF inválido")
        @Schema(example = "123.456.789-00")
        String cpf,

        @NotBlank(message = "E-mail é obrigatório")
        @Email(message = "E-mail inválido")
        @Size(max = 255, message = "E-mail deve ter no máximo 255 caracteres")
        @Schema(example = "collaborator@email.com")
        String email,

        @Size(max = 20, message = "Telefone deve ter no máximo 20 caracteres")
        @Schema(example = "31999998888")
        String phone,

        @Schema(example = "1")
        Long addressId,

        @NotNull(message = "Data de início do contrato é obrigatória")
        @Schema(example = "2024-01-15")
        LocalDate contractStartDate,

        @Schema(example = "2025-01-14")
        LocalDate contractEndDate,

        @NotBlank(message = "CEP é obrigatório")
        @Pattern(regexp = "\\d{5}-?\\d{3}", message = "CEP inválido")
        @Schema(example = "30140-072")
        String cep,

        @NotBlank(message = "Número é obrigatório")
        @Schema(example = "1000")
        String number,

        @Schema(example = "Apto 502")
        String complement
) {
}

package br.com.raroacademy.demo.domain.DTO.collaborator;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record CollaboratorRequestDTO(
        @NotBlank(message = "Name is required")
        @Size(max = 255, message = "Name must have at most 255 characters")
        @Schema(example = "Collaborator name")
        String name,

        @NotBlank(message = "CPF is required")
        @Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}|\\d{11}", message = "Invalid CPF")
        @Schema(example = "123.456.789-00")
        String cpf,

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email")
        @Size(max = 255, message = "E-mail must have at most 255 characters")
        @Schema(example = "collaborator@email.com")
        String email,

        @Size(max = 20, message = "Telephone must have at most 20 characters")
        @Schema(example = "31999998888")
        String phone,

        @Schema(example = "1")
        Long addressId,

        @NotNull(message = "Contract start date is required")
        @Schema(example = "2024-01-15")
        LocalDate contractStartDate,

        @Schema(example = "2025-01-14")
        LocalDate contractEndDate,

        @NotBlank(message = "CEP is required")
        @Pattern(regexp = "\\d{5}-?\\d{3}", message = "CEP inválido")
        @Schema(example = "30140-072")
        String cep,

        @NotBlank(message = "Number is required")
        @Schema(example = "1000")
        String number,

        @Schema(example = "Ap. 502")
        String complement
) {
}

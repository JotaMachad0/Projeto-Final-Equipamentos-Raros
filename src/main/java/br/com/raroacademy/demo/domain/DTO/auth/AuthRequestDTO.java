package br.com.raroacademy.demo.domain.DTO.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record AuthRequestDTO(
        @NotBlank
        @Length(max = 50)
        @Email(regexp = "^[\\w\\.-]+@[\\w\\.-]+\\.\\w{2,}$")
        @Schema(example = "email@teste.com")
        String email,

        @NotBlank
        @Length(min = 6, max = 255)
        @Schema(example = "your-password")
        String password
) {
}
package br.com.raroacademy.demo.domain.DTO.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record UserRequestDTO(
        @NotBlank
        @Length(max = 100)
        @Schema(example = "User Name")
        String name,

        @NotBlank
        @Length(max = 50)
        @Email(regexp = "^[\\w\\.-]+@[\\w\\.-]+\\.\\w{2,}$")
        @Schema(example = "email@teste.com")
        String email,

        @NotBlank
        @Length(min = 6, max = 50)
        @Schema(example = "your-password")
        String password) {
}

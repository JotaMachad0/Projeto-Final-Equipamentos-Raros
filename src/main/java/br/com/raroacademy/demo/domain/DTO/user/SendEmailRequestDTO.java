package br.com.raroacademy.demo.domain.DTO.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record SendEmailRequestDTO(
        @NotBlank
        @Length(max = 50)
        @Email(regexp = "^[\\w\\.-]+@[\\w\\.-]+\\.\\w{2,}$")
        @Schema(example = "email@example.com")
        String email
) {
}

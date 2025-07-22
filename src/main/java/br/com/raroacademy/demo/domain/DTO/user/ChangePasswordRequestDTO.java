package br.com.raroacademy.demo.domain.DTO.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.Length;

public record ChangePasswordRequestDTO(
        @NotBlank
        @Length(max = 50)
        @Email(regexp = "^[\\w\\.-]+@[\\w\\.-]+\\.\\w{2,}$")
        @Schema(example = "email@teste.com")
        String email,

        @NotBlank
        @Length(min = 6, max = 50)
        @Schema(example = "new-password")
        String newPassword,

        @NotNull
        @Min(100000)
        @Max(999999)
        @Schema(example = "123456")
        Long code
) {
}

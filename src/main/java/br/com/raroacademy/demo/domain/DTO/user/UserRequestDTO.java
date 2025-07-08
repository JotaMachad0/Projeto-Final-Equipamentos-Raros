package br.com.raroacademy.demo.domain.DTO.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record UserRequestDTO(
        @NotBlank
        @Length(max = 100)
        String name,

        @NotBlank
        @Length(max = 50)
        @Email(regexp = "^[\\w\\.-]+@[\\w\\.-]+\\.\\w{2,}$")
        String email,

        @NotBlank
        @Length(min = 6, max = 50)
        String password) {
}

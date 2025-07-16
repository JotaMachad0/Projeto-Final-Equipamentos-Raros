package br.com.raroacademy.demo.domain.DTO.address;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AddressRequestDTO(

        @NotBlank
        @Size(max = 9)
        @Pattern(regexp = "^\\d{5}-?\\d{3}$")
        @Schema(example = "01001-000", description = "ZIP code (CEP)")
        String zipCode,

        @NotBlank
        @Size(max = 255)
        @Schema(example = "Praça da Sé", description = "Street name")
        String street,

        @NotBlank
        @Size(max = 10)
        @Schema(example = "123", description = "Address number")
        String number,

        @Size(max = 255)
        @Schema(example = "lado ímpar", description = "Complement (optional)")
        String complement,

        @NotBlank
        @Size(max = 100)
        @Schema(example = "Sé", description = "Neighborhood")
        String neighborhood,

        @NotBlank
        @Size(max = 100)
        @Schema(example = "São Paulo", description = "City")
        String city,

        @NotBlank
        @Size(max = 2)
        @Schema(example = "SP", description = "State (UF)")
        String state
) {
}

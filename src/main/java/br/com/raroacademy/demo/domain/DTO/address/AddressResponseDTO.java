package br.com.raroacademy.demo.domain.DTO.address;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record AddressResponseDTO(

        @Schema(example = "1", description = "Address ID")
        Long id,

        @Schema(example = "Praça da Sé", description = "Street name")
        String street,

        @Schema(example = "Sé", description = "Neighborhood")
        String neighborhood,

        @Schema(example = "São Paulo", description = "City")
        String city,

        @Schema(example = "SP", description = "State (UF)")
        String state,

        @Schema(example = "01001-000", description = "ZIP code (CEP)")
        String cep,

        @Schema(example = "123", description = "Address number")
        String number,

        @Schema(example = "lado ímpar", description = "Complement")
        String complement,

        @Schema(example = "Brasil", description = "Country")
        String country
) {
}

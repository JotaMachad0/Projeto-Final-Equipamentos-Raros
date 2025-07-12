package br.com.raroacademy.demo.domain.DTO.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record UserResponseDTO(
        @Schema(example = "1")
        Long id,

        @Schema(example = "User Name")
        String name,

        @Schema(example = "teste@email.com")
        String email,

        @Schema(example = "false")
        Boolean emailConfirmed
) {
}

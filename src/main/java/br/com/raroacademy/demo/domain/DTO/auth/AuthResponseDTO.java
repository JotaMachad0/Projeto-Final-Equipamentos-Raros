package br.com.raroacademy.demo.domain.DTO.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record AuthResponseDTO(
        @Schema(example = "token")
        String token,

        @Schema(example = "token_type")
        String tokenType,

        @Schema(example = "expires_in")
        long expiresIn,

        @Schema(example = "user_email")
        String userEmail
) {
}
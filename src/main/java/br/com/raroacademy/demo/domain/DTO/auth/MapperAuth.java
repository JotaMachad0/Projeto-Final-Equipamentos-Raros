package br.com.raroacademy.demo.domain.DTO.auth;

import org.springframework.stereotype.Component;

@Component
public class MapperAuth {
    public AuthResponseDTO toResponseDTO(String token, String tokenType, long expiresIn, String userEmail) {
        return AuthResponseDTO.builder()
                .token(token)
                .tokenType(tokenType)
                .expiresIn(expiresIn)
                .userEmail(userEmail)
                .build();
    }
}
package br.com.raroacademy.demo.domain.DTO.user;

import lombok.Builder;

@Builder
public record UserResponseDTO(Long id, String name, String email, Boolean emailConfirmed) {
}

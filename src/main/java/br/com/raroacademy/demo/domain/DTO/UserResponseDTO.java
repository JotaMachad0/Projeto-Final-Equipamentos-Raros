package br.com.raroacademy.demo.domain.DTO;

import lombok.Builder;

@Builder
public record UserResponseDTO(Integer id, String name, String email, Boolean emailConfirmed) {
}

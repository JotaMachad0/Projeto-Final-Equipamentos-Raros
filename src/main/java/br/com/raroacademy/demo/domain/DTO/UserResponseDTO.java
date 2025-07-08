package br.com.raroacademy.demo.domain.DTO;

import br.com.raroacademy.demo.domain.entity.UserEntity;
import lombok.Builder;

@Builder
public record UserResponseDTO(Long id, String name, String email, Boolean emailConfirmed) {

    public static UserResponseDTO fromEntity(UserEntity user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .emailConfirmed(user.getEmailConfirmed())
                .build();
    }
}

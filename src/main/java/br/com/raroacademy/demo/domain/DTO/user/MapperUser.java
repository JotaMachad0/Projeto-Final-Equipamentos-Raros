package br.com.raroacademy.demo.domain.DTO.user;

import br.com.raroacademy.demo.domain.entities.UserEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MapperUser {

    public UserEntity toUser(UserRequestDTO request) {
        return UserEntity.builder()
                .name(request.name())
                .email(request.email())
                .password(request.password())
                .emailConfirmed(false)
                .build();
    }

    public UserResponseDTO toUserResponseDTO(UserEntity user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .emailConfirmed(user.getEmailConfirmed())
                .build();
    }

    public UserEntity toUpdateUser(UserEntity existing, UserRequestDTO request) {
        return UserEntity.builder()
                .id(existing.getId())
                .name(request.name())
                .email(request.email())
                .password(request.password())
                .emailConfirmed(
                        existing.getEmail().equals(request.email()) && Boolean.TRUE.equals(existing.getEmailConfirmed())
                )
                .build();
    }

    public List<UserResponseDTO> toUserList(List<UserEntity> userEntityList) {
        return userEntityList.stream().map(userEntity -> {
            return UserResponseDTO.builder()
                    .id(userEntity.getId())
                    .name(userEntity.getName())
                    .email(userEntity.getEmail())
                    .emailConfirmed(userEntity.getEmailConfirmed())
                    .build();
        }).toList();
    }
}

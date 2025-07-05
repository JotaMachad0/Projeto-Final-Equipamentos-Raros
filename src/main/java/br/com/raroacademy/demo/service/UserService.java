package br.com.raroacademy.demo.service;

import br.com.raroacademy.demo.domain.DTO.UserRequestDTO;
import br.com.raroacademy.demo.domain.DTO.UserResponseDTO;
import br.com.raroacademy.demo.domain.entity.UserEntity;
import br.com.raroacademy.demo.domain.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserResponseDTO create(UserRequestDTO request) {
        var user = mapperToUser(request);
        var userSaved = userRepository.save(user);
        return mapperToUserResponseDTO(userSaved);
    }

    private UserEntity mapperToUser(UserRequestDTO request) {
        return UserEntity.builder()
                .name(request.name())
                .email(request.email())
                .password(request.password())
                .emailConfirmed(false)
                .build();
    }

    private UserResponseDTO mapperToUserResponseDTO(UserEntity user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .emailConfirmed(user.getEmailConfirmed())
                .build();
    }
}

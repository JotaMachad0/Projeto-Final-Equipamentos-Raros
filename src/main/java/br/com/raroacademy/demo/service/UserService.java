package br.com.raroacademy.demo.service;

import br.com.raroacademy.demo.domain.DTO.UserRequestDTO;
import br.com.raroacademy.demo.domain.DTO.UserResponseDTO;
import br.com.raroacademy.demo.domain.entity.UserEntity;
import br.com.raroacademy.demo.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserResponseDTO create(UserRequestDTO dto) {
        UserEntity user = UserEntity.builder()
                .name(dto.name())
                .email(dto.email())
                .password(dto.password())
                .emailConfirmed(false)
                .build();

        UserEntity savedUser = userRepository.save(user);

        return mapperToUserResponseDTO(savedUser);
    }


    public UserResponseDTO getUserById(Long id) {
        return userRepository.findById(id)
                .map(this::mapperToUserResponseDTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
    }


    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::mapperToUserResponseDTO)
                .toList();
    }

    private UserResponseDTO mapperToUserResponseDTO(UserEntity user) {
        return UserResponseDTO.builder()
                .id(Long.valueOf(user.getId()))
                .name(user.getName())
                .email(user.getEmail())
                .emailConfirmed(user.getEmailConfirmed())
                .build();
    }
}

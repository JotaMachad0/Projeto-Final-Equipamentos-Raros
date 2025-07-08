package br.com.raroacademy.demo.service;

import br.com.raroacademy.demo.domain.DTO.UserRequestDTO;
import br.com.raroacademy.demo.domain.DTO.UserResponseDTO;
import br.com.raroacademy.demo.domain.entity.UserEntity;
import br.com.raroacademy.demo.domain.repository.UserRepository;
import br.com.raroacademy.demo.exception.BusinessException;
import br.com.raroacademy.demo.exception.NotFoundException;
import br.com.raroacademy.demo.exception.UserNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserResponseDTO create(UserRequestDTO request) {
        UserEntity user = UserEntity.builder()
                .name(request.name())
                .email(request.email())
                .password(request.password())
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

    @Transactional
    public UserResponseDTO update(Long id, @Valid UserRequestDTO request) {
        UserEntity existing = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

        if (!existing.getEmail().equals(request.email()) &&
                userRepository.existsByEmail(request.email())) {
            throw new BusinessException("E-mail já está em uso por outro usuário");
        }

        UserEntity updated = applyUpdates(existing, request);

        return mapperToUserResponseDTO(userRepository.save(updated));
    }

    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::mapperToUserResponseDTO)
                .toList();
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

    private UserEntity applyUpdates(UserEntity existing, UserRequestDTO request) {
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

    public void delete(Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado com o id: " + id));

        userRepository.delete(user);
    }
}

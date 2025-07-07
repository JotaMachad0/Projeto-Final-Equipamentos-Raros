package br.com.raroacademy.demo.service;

import br.com.raroacademy.demo.domain.DTO.UserRequestDTO;
import br.com.raroacademy.demo.domain.DTO.UserResponseDTO;
import br.com.raroacademy.demo.domain.entity.User;
import br.com.raroacademy.demo.domain.repository.UserRepository;
import br.com.raroacademy.demo.exception.NotFoundException;
import jakarta.validation.Valid;
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

    private User mapperToUser(UserRequestDTO request) {
        return User.builder()
                .name(request.name())
                .email(request.email())
                .password(request.password())
                .emailConfirmed(false)
                .build();
    }

    private UserResponseDTO mapperToUserResponseDTO(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .emailConfirmed(user.getEmailConfirmed())
                .build();
    }

    public UserResponseDTO update(Long id, @Valid UserRequestDTO request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

        user.setName(request.name());
        user.setEmail(request.email());

        User updated = userRepository.save(user);
        return UserResponseDTO.fromEntity(updated);
    }
}

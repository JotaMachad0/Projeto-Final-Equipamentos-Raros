package br.com.raroacademy.demo.service;

import br.com.raroacademy.demo.domain.DTO.user.MapperUser;
import br.com.raroacademy.demo.domain.DTO.user.UserRequestDTO;
import br.com.raroacademy.demo.domain.DTO.user.UserResponseDTO;
import br.com.raroacademy.demo.domain.entities.UserEntity;
import br.com.raroacademy.demo.repository.UserRepository;
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
    private final MapperUser mapperUser;

    public UserResponseDTO create(UserRequestDTO request) {
        var user = mapperUser.toUser(request);
        UserEntity savedUser = userRepository.save(user);
        return mapperUser.toUserResponseDTO(savedUser);
    }

    public UserResponseDTO getUserById(Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
        return mapperUser.toUserResponseDTO(user);
    }

    @Transactional
    public UserResponseDTO update(Long id, @Valid UserRequestDTO request) {
        var existing = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

        if (!existing.getEmail().equals(request.email()) &&
                userRepository.existsByEmail(request.email())) {
            throw new BusinessException("E-mail já está em uso por outro usuário");
        }
        var updated = mapperUser.toApplyUpdates(existing, request);
        return mapperUser.toUserResponseDTO(userRepository.save(updated));
    }

    public void delete(Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado com o id: " + id));
        userRepository.delete(user);
    }

    public List<UserResponseDTO> getAllUsers() {
        var userList = userRepository.findAll();
        return mapperUser.toUserList(userList);
    }
}

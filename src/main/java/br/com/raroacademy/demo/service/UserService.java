package br.com.raroacademy.demo.service;

import br.com.raroacademy.demo.commons.i18n.I18nUtil;
import br.com.raroacademy.demo.domain.DTO.user.MapperUser;
import br.com.raroacademy.demo.domain.DTO.user.UserRequestDTO;
import br.com.raroacademy.demo.domain.DTO.user.UserResponseDTO;
import br.com.raroacademy.demo.domain.entities.UserEntity;
import br.com.raroacademy.demo.exception.NotFoundException;
import br.com.raroacademy.demo.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final MapperUser mapperUser;
    private final I18nUtil i18n;

    public UserResponseDTO create(UserRequestDTO request) {
        var user = mapperUser.toUser(request);
        UserEntity savedUser = userRepository.save(user);
        return mapperUser.toUserResponseDTO(savedUser);
    }

    public UserResponseDTO getUserById(Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(i18n.getMessage("user.not.found")));
        return mapperUser.toUserResponseDTO(user);
    }

    @Transactional
    public UserResponseDTO update(Long id, @Valid UserRequestDTO request) {
        var existing = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(i18n.getMessage("user.not.found")));

        if (!existing.getEmail().equals(request.email()) &&
                userRepository.existsByEmail(request.email())) {
            throw new DataIntegrityViolationException(i18n.getMessage("user.email.already.exists"));
        }
        var updated = mapperUser.toUpdateUser(existing, request);
        return mapperUser.toUserResponseDTO(userRepository.save(updated));
    }

    public void delete(Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(i18n.getMessage("user.not.found")));
        userRepository.delete(user);
    }

    public List<UserResponseDTO> getAllUsers() {
        var userList = userRepository.findAll();
        return mapperUser.toUserList(userList);
    }
}

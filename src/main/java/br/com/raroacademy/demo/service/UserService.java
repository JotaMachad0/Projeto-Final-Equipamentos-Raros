package br.com.raroacademy.demo.service;

import br.com.raroacademy.demo.commons.email.EmailBody;
import br.com.raroacademy.demo.commons.i18n.I18nUtil;
import br.com.raroacademy.demo.domain.DTO.user.*;
import br.com.raroacademy.demo.exception.InvalidArgumentException;
import br.com.raroacademy.demo.exception.NotFoundException;
import br.com.raroacademy.demo.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final MapperUser mapperUser;
    private final I18nUtil i18n;
    private final EmailBody emailBody;
    private final CodeService codesService;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponseDTO create(UserRequestDTO request) {
        var user = mapperUser.toUser(request);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        try {
            var savedUser = userRepository.save(user);

            var confirmationToken = codesService.addUserAndCode(savedUser);
            try {
                emailBody.sendConfirmationEmail(savedUser.getEmail(), confirmationToken);
            } catch (Exception e) {
                throw new RuntimeException(i18n.getMessage("error.sending.email"));
            }

            return mapperUser.toUserResponseDTO(savedUser);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException(i18n.getMessage("user.email.already.exists"));
        }
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

        updated.setPassword(passwordEncoder.encode(request.password()));
        
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

    @Transactional
    public void sendEmailResetPassword(SendEmailRequestDTO request) {
        var user = userRepository.findByEmail(request.email());
        if (user == null) {
            throw new NotFoundException(i18n.getMessage("user.does.not.have.with.email"));
        }

        var tokenResetPassword = codesService.addUserAndCode(user);
        try {
            emailBody.sendEmail(user.getEmail(), tokenResetPassword);
        } catch (Exception e) {
            throw new RuntimeException(i18n.getMessage("error.sending.email"));
        }
    }

    @Transactional
    public void changePassword(ChangePasswordRequestDTO request) {
        var user = userRepository.findByEmail(request.email());
        if (user == null) {
            throw new NotFoundException(i18n.getMessage("user.does.not.have.with.email"));
        }

        codesService.verifyAndConfirmCode(request.code(), user);

        user.setPassword(passwordEncoder.encode(request.newPassword()));

        userRepository.save(user);
    }

    @Transactional
    public void confirmEmail(String email, Long token) {
        var user = userRepository.findByEmail(email);
        if (user == null) {
            throw new NotFoundException(i18n.getMessage("user.does.not.have.with.email"));
        }

        codesService.confirmCode(token, user);
        user.setEmailConfirmed(true);

        userRepository.save(user);
    }

    @Transactional
    public void resendConfirmationEmail(SendEmailRequestDTO request) {
        var user = userRepository.findByEmail(request.email());
        if (user == null) {
            throw new NotFoundException(i18n.getMessage("user.does.not.have.with.email"));
        }

        if (user.getEmailConfirmed()) {
            throw new InvalidArgumentException(i18n.getMessage("user.email.already.confirmed"));
        }

        var confirmationToken = codesService.addUserAndCode(user);
        try {
            emailBody.sendConfirmationEmail(user.getEmail(), confirmationToken);
        } catch (Exception e) {
            throw new RuntimeException(i18n.getMessage("error.sending.email"));
        }
    }
}

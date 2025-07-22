package br.com.raroacademy.demo.service;

import br.com.raroacademy.demo.commons.email.EmailBody;
import br.com.raroacademy.demo.commons.i18n.I18nUtil;
import br.com.raroacademy.demo.domain.DTO.user.*;
import br.com.raroacademy.demo.domain.entities.UserEntity;
import br.com.raroacademy.demo.exception.CodeException;
import br.com.raroacademy.demo.exception.InvalidArgumentException;
import br.com.raroacademy.demo.exception.NotFoundException;
import br.com.raroacademy.demo.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private MapperUser mapperUser;

    @Mock
    private I18nUtil i18n;

    @Mock
    private EmailBody emailBody;

    @Mock
    private CodeService codesService;
    
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private UserEntity userEntity;
    private UserRequestDTO userRequestDTO;
    private UserResponseDTO userResponseDTO;
    private SendEmailRequestDTO sendEmailRequestDTO;
    private ChangePasswordRequestDTO changePasswordRequestDTO;

    @BeforeEach
    void setUp() {
        userEntity = UserEntity.builder()
                .id(1L)
                .name("Test User")
                .email("test@example.com")
                .password("password123")
                .emailConfirmed(false)
                .build();

        userRequestDTO = new UserRequestDTO(
                "Test User",
                "test@example.com",
                "password123"
        );

        userResponseDTO = UserResponseDTO.builder()
                .id(1L)
                .name("Test User")
                .email("test@example.com")
                .emailConfirmed(false)
                .build();

        sendEmailRequestDTO = new SendEmailRequestDTO("test@example.com");
        
        changePasswordRequestDTO = new ChangePasswordRequestDTO(
                "test@example.com",
                "newpassword123",
                123456L
        );

        when(i18n.getMessage("user.not.found")).thenReturn("User not found");
        when(i18n.getMessage("user.email.already.exists")).thenReturn("Email already exists");
        when(i18n.getMessage("user.does.not.have.with.email")).thenReturn("User with this email does not exist");
        when(i18n.getMessage("error.sending.email")).thenReturn("Error sending email");
        when(i18n.getMessage("user.email.already.confirmed")).thenReturn("Email already confirmed");
        when(i18n.getMessage("code.invalid")).thenReturn("Invalid code");
        when(i18n.getMessage("code.expired")).thenReturn("Code expired");
        when(i18n.getMessage("code.already.used")).thenReturn("Code already used");
        
        // Setup passwordEncoder mock
        when(passwordEncoder.encode(anyString())).thenAnswer(invocation -> {
            String rawPassword = invocation.getArgument(0);
            return "encoded_" + rawPassword;
        });
    }

    @Test
    void create_Success() {
        // Arrange
        when(mapperUser.toUser(userRequestDTO)).thenReturn(userEntity);
        when(userRepository.save(userEntity)).thenReturn(userEntity);
        when(mapperUser.toUserResponseDTO(userEntity)).thenReturn(userResponseDTO);

        // Act
        UserResponseDTO result = userService.create(userRequestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(userResponseDTO, result);
        verify(mapperUser).toUser(userRequestDTO);
        verify(userRepository).save(userEntity);
        verify(mapperUser).toUserResponseDTO(userEntity);
    }

    @Test
    void create_EmailAlreadyExists() {
        // Arrange
        when(mapperUser.toUser(userRequestDTO)).thenReturn(userEntity);
        when(userRepository.save(userEntity)).thenThrow(new DataIntegrityViolationException("Email already exists"));

        // Act & Assert
        DataIntegrityViolationException exception = assertThrows(
                DataIntegrityViolationException.class,
                () -> userService.create(userRequestDTO)
        );
        assertEquals("Email already exists", exception.getMessage());
        verify(mapperUser).toUser(userRequestDTO);
        verify(userRepository).save(userEntity);
    }

    @Test
    void getUserById_Success() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));
        when(mapperUser.toUserResponseDTO(userEntity)).thenReturn(userResponseDTO);

        // Act
        UserResponseDTO result = userService.getUserById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(userResponseDTO, result);
        verify(userRepository).findById(1L);
        verify(mapperUser).toUserResponseDTO(userEntity);
    }

    @Test
    void getUserById_NotFound() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> userService.getUserById(1L)
        );
        assertEquals("User not found", exception.getMessage());
        verify(userRepository).findById(1L);
    }

    @Test
    void update_Success() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));
        when(userRepository.existsByEmail(userRequestDTO.email())).thenReturn(false);
        when(mapperUser.toUpdateUser(userEntity, userRequestDTO)).thenReturn(userEntity);
        when(userRepository.save(userEntity)).thenReturn(userEntity);
        when(mapperUser.toUserResponseDTO(userEntity)).thenReturn(userResponseDTO);

        // Act
        UserResponseDTO result = userService.update(1L, userRequestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(userResponseDTO, result);
        verify(userRepository).findById(1L);
        verify(mapperUser).toUpdateUser(userEntity, userRequestDTO);
        verify(userRepository).save(userEntity);
        verify(mapperUser).toUserResponseDTO(userEntity);
    }

    @Test
    void update_EmailAlreadyExists() {
        // Arrange
        UserRequestDTO newEmailRequest = new UserRequestDTO(
                "Test User",
                "newemail@example.com",
                "password123"
        );

        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));
        when(userRepository.existsByEmail(newEmailRequest.email())).thenReturn(true);

        // Act & Assert
        DataIntegrityViolationException exception = assertThrows(
                DataIntegrityViolationException.class,
                () -> userService.update(1L, newEmailRequest)
        );
        assertEquals("Email already exists", exception.getMessage());
        verify(userRepository).findById(1L);
        verify(userRepository).existsByEmail(newEmailRequest.email());
    }

    @Test
    void update_UserNotFound() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> userService.update(1L, userRequestDTO)
        );
        assertEquals("User not found", exception.getMessage());
        verify(userRepository).findById(1L);
    }

    @Test
    void delete_Success() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));

        // Act
        userService.delete(1L);

        // Assert
        verify(userRepository).findById(1L);
        verify(userRepository).delete(userEntity);
    }

    @Test
    void delete_UserNotFound() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> userService.delete(1L)
        );
        assertEquals("User not found", exception.getMessage());
        verify(userRepository).findById(1L);
        verify(userRepository, never()).delete(any());
    }

    @Test
    void getAllUsers_Success() {
        // Arrange
        List<UserEntity> userEntities = Arrays.asList(userEntity);
        List<UserResponseDTO> userResponseDTOs = Arrays.asList(userResponseDTO);

        when(userRepository.findAll()).thenReturn(userEntities);
        when(mapperUser.toUserList(userEntities)).thenReturn(userResponseDTOs);

        // Act
        List<UserResponseDTO> result = userService.getAllUsers();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(userResponseDTO, result.get(0));
        verify(userRepository).findAll();
        verify(mapperUser).toUserList(userEntities);
    }

    @Test
    void sendEmailResetPassword_Success() throws Exception {
        // Arrange
        when(userRepository.findByEmail(sendEmailRequestDTO.email())).thenReturn(userEntity);
        when(codesService.addUserAndCode(userEntity)).thenReturn(123456L);

        // Act
        userService.sendEmailResetPassword(sendEmailRequestDTO);

        // Assert
        verify(userRepository).findByEmail(sendEmailRequestDTO.email());
        verify(codesService).addUserAndCode(userEntity);
        verify(emailBody).sendEmail(userEntity.getEmail(), 123456L);
    }

    @Test
    void sendEmailResetPassword_UserNotFound() {
        // Arrange
        when(userRepository.findByEmail(sendEmailRequestDTO.email())).thenReturn(null);

        // Act & Assert
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> userService.sendEmailResetPassword(sendEmailRequestDTO)
        );
        assertEquals("User with this email does not exist", exception.getMessage());
        verify(userRepository).findByEmail(sendEmailRequestDTO.email());
        verify(codesService, never()).addUserAndCode(any());
        try {
            verify(emailBody, never()).sendEmail(anyString(), anyLong());
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }

    @Test
    void sendEmailResetPassword_EmailError() throws Exception {
        // Arrange
        when(userRepository.findByEmail(sendEmailRequestDTO.email())).thenReturn(userEntity);
        when(codesService.addUserAndCode(userEntity)).thenReturn(123456L);
        doThrow(new RuntimeException("Email error")).when(emailBody).sendEmail(anyString(), anyLong());

        // Act & Assert
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> userService.sendEmailResetPassword(sendEmailRequestDTO)
        );
        assertEquals("Error sending email", exception.getMessage());
        verify(userRepository).findByEmail(sendEmailRequestDTO.email());
        verify(codesService).addUserAndCode(userEntity);
        verify(emailBody).sendEmail(userEntity.getEmail(), 123456L);
    }

    @Test
    void changePassword_Success() {
        // Arrange
        when(userRepository.findByEmail(changePasswordRequestDTO.email())).thenReturn(userEntity);
        
        // Act
        userService.changePassword(changePasswordRequestDTO);
        
        // Assert
        verify(userRepository).findByEmail(changePasswordRequestDTO.email());
        verify(codesService).verifyAndConfirmCode(changePasswordRequestDTO.code(), userEntity);
        verify(passwordEncoder).encode(changePasswordRequestDTO.newPassword());
        verify(userRepository).save(userEntity);
        assertEquals("encoded_" + changePasswordRequestDTO.newPassword(), userEntity.getPassword());
    }
    
    @Test
    void changePassword_UserNotFound() {
        // Arrange
        when(userRepository.findByEmail(changePasswordRequestDTO.email())).thenReturn(null);
        
        // Act & Assert
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> userService.changePassword(changePasswordRequestDTO)
        );
        assertEquals("User with this email does not exist", exception.getMessage());
        verify(userRepository).findByEmail(changePasswordRequestDTO.email());
        verify(codesService, never()).verifyAndConfirmCode(anyLong(), any());
        verify(userRepository, never()).save(any());
    }
    
    @Test
    void changePassword_CodeVerificationFailure() {
        // Arrange
        when(userRepository.findByEmail(changePasswordRequestDTO.email())).thenReturn(userEntity);
        doThrow(new CodeException("Invalid code")).when(codesService).verifyAndConfirmCode(anyLong(), any());
        
        // Act & Assert
        CodeException exception = assertThrows(
                CodeException.class,
                () -> userService.changePassword(changePasswordRequestDTO)
        );
        assertEquals("Invalid code", exception.getMessage());
        verify(userRepository).findByEmail(changePasswordRequestDTO.email());
        verify(codesService).verifyAndConfirmCode(changePasswordRequestDTO.code(), userEntity);
        verify(userRepository, never()).save(any());
    }
    
    @Test
    void confirmEmail_Success() {
        // Arrange
        String email = "test@example.com";
        Long token = 123456L;
        when(userRepository.findByEmail(email)).thenReturn(userEntity);
        
        // Act
        userService.confirmEmail(email, token);
        
        // Assert
        verify(userRepository).findByEmail(email);
        verify(codesService).confirmCode(token, userEntity);
        verify(userRepository).save(userEntity);
        assertTrue(userEntity.getEmailConfirmed());
    }
    
    @Test
    void confirmEmail_UserNotFound() {
        // Arrange
        String email = "nonexistent@example.com";
        Long token = 123456L;
        when(userRepository.findByEmail(email)).thenReturn(null);
        
        // Act & Assert
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> userService.confirmEmail(email, token)
        );
        assertEquals("User with this email does not exist", exception.getMessage());
        verify(userRepository).findByEmail(email);
        verify(codesService, never()).confirmCode(anyLong(), any());
        verify(userRepository, never()).save(any());
    }
    
    @Test
    void confirmEmail_CodeConfirmationFailure() {
        // Arrange
        String email = "test@example.com";
        Long token = 123456L;
        when(userRepository.findByEmail(email)).thenReturn(userEntity);
        doThrow(new CodeException("Invalid code")).when(codesService).confirmCode(anyLong(), any());
        
        // Act & Assert
        CodeException exception = assertThrows(
                CodeException.class,
                () -> userService.confirmEmail(email, token)
        );
        assertEquals("Invalid code", exception.getMessage());
        verify(userRepository).findByEmail(email);
        verify(codesService).confirmCode(token, userEntity);
        verify(userRepository, never()).save(any());
    }
    
    @Test
    void resendConfirmationEmail_Success() throws Exception {
        // Arrange
        when(userRepository.findByEmail(sendEmailRequestDTO.email())).thenReturn(userEntity);
        when(codesService.addUserAndCode(userEntity)).thenReturn(123456L);
        
        // Act
        userService.resendConfirmationEmail(sendEmailRequestDTO);
        
        // Assert
        verify(userRepository).findByEmail(sendEmailRequestDTO.email());
        verify(codesService).addUserAndCode(userEntity);
        verify(emailBody).sendConfirmationEmail(userEntity.getEmail(), 123456L);
    }
    
    @Test
    void resendConfirmationEmail_UserNotFound() {
        // Arrange
        when(userRepository.findByEmail(sendEmailRequestDTO.email())).thenReturn(null);
        
        // Act & Assert
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> userService.resendConfirmationEmail(sendEmailRequestDTO)
        );
        assertEquals("User with this email does not exist", exception.getMessage());
        verify(userRepository).findByEmail(sendEmailRequestDTO.email());
        verify(codesService, never()).addUserAndCode(any());
        try {
            verify(emailBody, never()).sendConfirmationEmail(anyString(), anyLong());
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }
    
    @Test
    void resendConfirmationEmail_EmailAlreadyConfirmed() {
        // Arrange
        userEntity.setEmailConfirmed(true);
        when(userRepository.findByEmail(sendEmailRequestDTO.email())).thenReturn(userEntity);
        
        // Act & Assert
        InvalidArgumentException exception = assertThrows(
                InvalidArgumentException.class,
                () -> userService.resendConfirmationEmail(sendEmailRequestDTO)
        );
        assertEquals("Email already confirmed", exception.getMessage());
        verify(userRepository).findByEmail(sendEmailRequestDTO.email());
        verify(codesService, never()).addUserAndCode(any());
        try {
            verify(emailBody, never()).sendConfirmationEmail(anyString(), anyLong());
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }
    
    @Test
    void resendConfirmationEmail_EmailError() throws Exception {
        // Arrange
        when(userRepository.findByEmail(sendEmailRequestDTO.email())).thenReturn(userEntity);
        when(codesService.addUserAndCode(userEntity)).thenReturn(123456L);
        doThrow(new RuntimeException("Email error")).when(emailBody).sendConfirmationEmail(anyString(), anyLong());
        
        // Act & Assert
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> userService.resendConfirmationEmail(sendEmailRequestDTO)
        );
        assertEquals("Error sending email", exception.getMessage());
        verify(userRepository).findByEmail(sendEmailRequestDTO.email());
        verify(codesService).addUserAndCode(userEntity);
        verify(emailBody).sendConfirmationEmail(userEntity.getEmail(), 123456L);
    }
}

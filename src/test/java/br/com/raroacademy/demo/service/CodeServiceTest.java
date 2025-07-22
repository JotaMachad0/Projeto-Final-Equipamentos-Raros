package br.com.raroacademy.demo.service;

import br.com.raroacademy.demo.commons.i18n.I18nUtil;
import br.com.raroacademy.demo.domain.entities.CodeEntity;
import br.com.raroacademy.demo.domain.entities.UserEntity;
import br.com.raroacademy.demo.exception.CodeException;
import br.com.raroacademy.demo.repository.CodeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class CodeServiceTest {

    @Mock
    private CodeRepository codeRepository;

    @Mock
    private I18nUtil i18n;

    @InjectMocks
    private CodeService codeService;

    private UserEntity userEntity;
    private CodeEntity codeEntity;
    private LocalDateTime now;
    private LocalDateTime expiresAt;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.now();
        expiresAt = now.plusHours(2);

        userEntity = UserEntity.builder()
                .id(1L)
                .name("Test User")
                .email("test@example.com")
                .password("password123")
                .emailConfirmed(false)
                .build();

        codeEntity = CodeEntity.builder()
                .id(1L)
                .token("123456")
                .createdAt(now)
                .expiresAt(expiresAt)
                .used(false)
                .user(userEntity)
                .build();

        // Setup i18n messages
        when(i18n.getMessage("code.invalid")).thenReturn("Invalid code");
        when(i18n.getMessage("code.expired")).thenReturn("Code expired");
        when(i18n.getMessage("code.already.used")).thenReturn("Code already used");
    }

    @Test
    void addUserAndCode_NewCode() {
        // Arrange
        when(codeRepository.findByUser(userEntity)).thenReturn(null);
        when(codeRepository.save(any(CodeEntity.class))).thenAnswer(invocation -> {
            CodeEntity savedCode = invocation.getArgument(0);
            savedCode.setId(1L);
            return savedCode;
        });

        // Act
        Long result = codeService.addUserAndCode(userEntity);

        // Assert
        assertNotNull(result);
        assertTrue(result >= 100000 && result <= 999999);
        verify(codeRepository).findByUser(userEntity);
        verify(codeRepository).save(any(CodeEntity.class));
    }

    @Test
    void addUserAndCode_ExistingCode() {
        // Arrange
        when(codeRepository.findByUser(userEntity)).thenReturn(codeEntity);
        when(codeRepository.save(codeEntity)).thenReturn(codeEntity);

        // Act
        Long result = codeService.addUserAndCode(userEntity);

        // Assert
        assertNotNull(result);
        assertTrue(result >= 100000 && result <= 999999);
        verify(codeRepository).findByUser(userEntity);
        verify(codeRepository).save(codeEntity);
        assertFalse(codeEntity.getUsed());
        assertNotNull(codeEntity.getExpiresAt());
    }

    @Test
    void verifyAndConfirmCode_Success() {
        // Arrange
        Long code = 123456L;
        when(codeRepository.findByUser(userEntity)).thenReturn(codeEntity);

        // Act
        codeService.verifyAndConfirmCode(code, userEntity);

        // Assert
        verify(codeRepository).findByUser(userEntity);
        assertTrue(codeEntity.getUsed());
    }

    @Test
    void verifyAndConfirmCode_CodeNotFound() {
        // Arrange
        Long code = 123456L;
        when(codeRepository.findByUser(userEntity)).thenReturn(null);

        // Act & Assert
        CodeException exception = assertThrows(
                CodeException.class,
                () -> codeService.verifyAndConfirmCode(code, userEntity)
        );
        assertEquals("Invalid code", exception.getMessage());
        verify(codeRepository).findByUser(userEntity);
    }

    @Test
    void verifyAndConfirmCode_InvalidCode() {
        // Arrange
        Long code = 654321L;
        when(codeRepository.findByUser(userEntity)).thenReturn(codeEntity);

        // Act & Assert
        CodeException exception = assertThrows(
                CodeException.class,
                () -> codeService.verifyAndConfirmCode(code, userEntity)
        );
        assertEquals("Invalid code", exception.getMessage());
        verify(codeRepository).findByUser(userEntity);
    }

    @Test
    void verifyAndConfirmCode_ExpiredCode() {
        // Arrange
        Long code = 123456L;
        codeEntity.setExpiresAt(now.minusHours(1)); // Expired code
        when(codeRepository.findByUser(userEntity)).thenReturn(codeEntity);

        // Act & Assert
        CodeException exception = assertThrows(
                CodeException.class,
                () -> codeService.verifyAndConfirmCode(code, userEntity)
        );
        assertEquals("Code expired", exception.getMessage());
        verify(codeRepository).findByUser(userEntity);
    }

    @Test
    void verifyAndConfirmCode_AlreadyUsed() {
        // Arrange
        Long code = 123456L;
        codeEntity.setUsed(true);
        when(codeRepository.findByUser(userEntity)).thenReturn(codeEntity);

        // Act & Assert
        CodeException exception = assertThrows(
                CodeException.class,
                () -> codeService.verifyAndConfirmCode(code, userEntity)
        );
        assertEquals("Code already used", exception.getMessage());
        verify(codeRepository).findByUser(userEntity);
    }

    @Test
    void confirmCode_Success() {
        // Arrange
        Long token = 123456L;
        when(codeRepository.findByUser(userEntity)).thenReturn(codeEntity);

        // Act
        codeService.confirmCode(token, userEntity);

        // Assert
        verify(codeRepository).findByUser(userEntity);
    }

    @Test
    void confirmCode_InvalidCode() {
        // Arrange
        Long token = 654321L;
        when(codeRepository.findByUser(userEntity)).thenReturn(codeEntity);

        // Act & Assert
        CodeException exception = assertThrows(
                CodeException.class,
                () -> codeService.confirmCode(token, userEntity)
        );
        assertEquals("Invalid code", exception.getMessage());
        verify(codeRepository).findByUser(userEntity);
    }
}
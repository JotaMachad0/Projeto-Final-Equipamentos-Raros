package br.com.raroacademy.demo.service;

import br.com.raroacademy.demo.commons.email.EmailBody;
import br.com.raroacademy.demo.commons.email.StockAlertEmailBuilder;
import br.com.raroacademy.demo.commons.i18n.I18nUtil;
import br.com.raroacademy.demo.domain.entities.StockAlertEntity;
import br.com.raroacademy.demo.domain.entities.StockEntity;
import br.com.raroacademy.demo.domain.entities.UserEntity;
import br.com.raroacademy.demo.domain.enums.EquipmentType;
import br.com.raroacademy.demo.domain.enums.StockAlertStatus;
import br.com.raroacademy.demo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class EmailStockAlertServiceTest {

    @Mock
    private EmailBody emailBody;

    @Mock
    private UserRepository userRepository;

    @Mock
    private I18nUtil i18nUtil;

    @Mock
    private StockAlertEmailBuilder emailBuilder;

    @InjectMocks
    private EmailStockAlertService emailStockAlertService;

    private StockEntity stockEntity;
    private StockAlertEntity stockAlertEntity;
    private List<UserEntity> confirmedUsers;
    private String htmlContent;

    @BeforeEach
    void setUp() {
        stockEntity = StockEntity.builder()
                .id(1L)
                .equipmentType(EquipmentType.NOTEBOOK)
                .currentStock(5)
                .minStock(10)
                .securityStock(15)
                .build();

        stockAlertEntity = StockAlertEntity.builder()
                .id(1L)
                .stock(stockEntity)
                .alertSentAt(Timestamp.from(Instant.now()))
                .stockAlertStatus(StockAlertStatus.CREATED)
                .build();
        UserEntity user1 = UserEntity.builder()
                .id(1L)
                .name("User 1")
                .email("user1@example.com")
                .emailConfirmed(true)
                .build();

        UserEntity user2 = UserEntity.builder()
                .id(2L)
                .name("User 2")
                .email("user2@example.com")
                .emailConfirmed(true)
                .build();

        confirmedUsers = Arrays.asList(user1, user2);

        htmlContent = "<html><body>Test Email Content</body></html>";

        when(i18nUtil.getMessage("equipmenttype.notebook")).thenReturn("NOTEBOOK");
        when(i18nUtil.getMessage("stock.alert.email.subject")).thenReturn(
                "Alert: stock under the security levels for the equipment: {0}."
        );

        when(emailBuilder.buildHtmlContent(stockAlertEntity)).thenReturn(htmlContent);
    }

    @Test
    void sendStockAlertEmail_Success() throws Exception {
        // Arrange
        when(userRepository.findAllByEmailConfirmedTrue()).thenReturn(confirmedUsers);

        // Act
        emailStockAlertService.sendStockAlertEmail(stockAlertEntity);

        // Assert
        verify(userRepository).findAllByEmailConfirmedTrue();
        verify(i18nUtil).getMessage("equipmenttype.notebook");
        verify(i18nUtil).getMessage("stock.alert.email.subject");
        verify(emailBuilder).buildHtmlContent(stockAlertEntity);

        verify(emailBody).sendHtmlEmail("user1@example.com",
                "Alert: stock under the security levels for the equipment: NOTEBOOK.", htmlContent
        );
        verify(emailBody).sendHtmlEmail("user2@example.com",
                "Alert: stock under the security levels for the equipment: NOTEBOOK.", htmlContent
        );
    }

    @Test
    void sendStockAlertEmail_NoConfirmedUsers() throws Exception {
        // Arrange
        when(userRepository.findAllByEmailConfirmedTrue()).thenReturn(Collections.emptyList());

        // Act
        emailStockAlertService.sendStockAlertEmail(stockAlertEntity);

        // Assert
        verify(userRepository).findAllByEmailConfirmedTrue();
        verify(i18nUtil).getMessage("equipmenttype.notebook");
        verify(i18nUtil).getMessage("stock.alert.email.subject");
        verify(emailBuilder).buildHtmlContent(stockAlertEntity);

        verify(emailBody, never()).sendHtmlEmail(anyString(), anyString(), anyString());
    }

    @Test
    void sendStockAlertEmail_EmailError() throws Exception {
        // Arrange
        when(userRepository.findAllByEmailConfirmedTrue()).thenReturn(confirmedUsers);
        doThrow(new RuntimeException("Email error")).when(emailBody)
                .sendHtmlEmail(eq("user1@example.com"), anyString(), anyString());

        // Act & Assert
        Exception exception = assertThrows(
                Exception.class,
                () -> emailStockAlertService.sendStockAlertEmail(stockAlertEntity)
        );

        verify(emailBody).sendHtmlEmail("user1@example.com",
                "Alert: stock under the security levels for the equipment: NOTEBOOK.", htmlContent
        );

        verify(emailBody, never()).sendHtmlEmail("user2@example.com",
                "Alert: stock under the security levels for the equipment: NOTEBOOK.", htmlContent
        );
    }
}
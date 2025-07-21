package br.com.raroacademy.demo.service;

import br.com.raroacademy.demo.domain.entities.StockAlertEntity;
import br.com.raroacademy.demo.domain.entities.StockEntity;
import br.com.raroacademy.demo.domain.enums.EquipmentType;
import br.com.raroacademy.demo.domain.enums.StockAlertStatus;
import br.com.raroacademy.demo.repository.StockAlertRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;

import static org.mockito.Mockito.*;

class StockAlertSchedulerTest {

    @Mock
    private StockAlertRepository stockAlertRepository;

    @Mock
    private EmailStockAlertService emailStockAlertService;

    @InjectMocks
    private StockAlertScheduler scheduler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void sendReminderEmails_ShouldSendEmailsForAllCreatedAlerts() throws Exception {
        // Arrange
        StockEntity stock = StockEntity.builder()
                .equipmentType(EquipmentType.MONITOR)
                .build();

        StockAlertEntity alert = StockAlertEntity.builder()
                .stockAlertStatus(StockAlertStatus.CREATED)
                .stock(stock)
                .build();

        when(stockAlertRepository.findByStockAlertStatus(StockAlertStatus.CREATED))
                .thenReturn(List.of(alert));

        // Act
        scheduler.sendReminderEmails();

        // Assert
        verify(emailStockAlertService).sendStockAlertEmail(alert);
    }

    @Test
    void sendReminderEmails_ShouldHandleExceptionWithoutCrashing() throws Exception {
        // Arrange
        StockEntity stock = StockEntity.builder()
                .equipmentType(EquipmentType.MONITOR)
                .build();

        StockAlertEntity alert = StockAlertEntity.builder()
                .stockAlertStatus(StockAlertStatus.CREATED)
                .stock(stock)
                .build();

        when(stockAlertRepository.findByStockAlertStatus(StockAlertStatus.CREATED))
                .thenReturn(List.of(alert));

        doThrow(new RuntimeException("Simulated error"))
                .when(emailStockAlertService).sendStockAlertEmail(alert);

        // Act
        scheduler.sendReminderEmails();

        // Assert
        verify(emailStockAlertService).sendStockAlertEmail(alert);
    }

    @Test
    void sendReminderEmails_ShouldDoNothingIfNoCreatedAlerts() {
        // Arrange
        when(stockAlertRepository.findByStockAlertStatus(StockAlertStatus.CREATED))
                .thenReturn(List.of());

        // Act
        scheduler.sendReminderEmails();

        // Assert
        verifyNoInteractions(emailStockAlertService);
    }
}
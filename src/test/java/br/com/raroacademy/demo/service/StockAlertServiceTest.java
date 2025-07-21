package br.com.raroacademy.demo.service;

import br.com.raroacademy.demo.commons.i18n.I18nUtil;
import br.com.raroacademy.demo.domain.DTO.stock.allert.MapperStockAlert;
import br.com.raroacademy.demo.domain.DTO.stock.allert.StockAlertResponseDTO;
import br.com.raroacademy.demo.domain.entities.StockAlertEntity;
import br.com.raroacademy.demo.domain.entities.StockEntity;
import br.com.raroacademy.demo.domain.enums.EquipmentType;
import br.com.raroacademy.demo.domain.enums.StockAlertStatus;
import br.com.raroacademy.demo.exception.InvalidStatusException;
import br.com.raroacademy.demo.exception.NotFoundException;
import br.com.raroacademy.demo.repository.StockAlertRepository;
import br.com.raroacademy.demo.repository.StockRepository;
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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class StockAlertServiceTest {

    @Mock
    private StockAlertRepository stockAlertRepository;

    @Mock
    private StockRepository stockRepository;

    @Mock
    private MapperStockAlert mapperStockAlert;

    @Mock
    private EmailStockAlertService emailStockAlertService;

    @Mock
    private I18nUtil i18nUtil;

    @InjectMocks
    private StockAlertService stockAlertService;

    private StockEntity stockEntity;
    private StockAlertEntity stockAlertEntity;
    private StockAlertResponseDTO stockAlertResponseDTO;
    private Timestamp now;

    @BeforeEach
    void setUp() {
        now = Timestamp.from(Instant.now());

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
                .alertSentAt(now)
                .stockAlertStatus(StockAlertStatus.CREATED)
                .build();

        stockAlertResponseDTO = StockAlertResponseDTO.builder()
                .id(1L)
                .equipmentType(EquipmentType.NOTEBOOK)
                .currentStock(5)
                .securityStock(15)
                .alertSentAt(now)
                .stockAlertStatus(StockAlertStatus.CREATED)
                .build();

        when(i18nUtil.getMessage(eq("stock.alert.not.found"))).thenReturn("Stock alert not found.");
        when(i18nUtil.getMessage(eq("stock.alert.invalid.status"), eq("PROCESSED"))).thenReturn("Stock alert with the status {0} can't be processed.");
        when(i18nUtil.getMessage(eq("stock.alert.invalid.status"), eq("RESOLVED"))).thenReturn("Stock alert with the status {0} can't be processed.");
        when(i18nUtil.getMessage(eq("stock.alert.email.error"))).thenReturn("Error sending stock alert email.");
        when(i18nUtil.getMessage(eq("stock.alert.resolved.auto"), eq(1L))).thenReturn("Stock alert with the ID {0} automatically updated to: RESOLVED.");
        when(i18nUtil.getMessage(matches("stock\\.alert\\.status\\..*"))).thenReturn("PROCESSED");
    }

    @Test
    void getStockAlertById_Success() {
        // Arrange
        when(stockAlertRepository.findById(1L)).thenReturn(Optional.of(stockAlertEntity));
        when(mapperStockAlert.toResponseDTO(stockAlertEntity)).thenReturn(stockAlertResponseDTO);

        // Act
        StockAlertResponseDTO result = stockAlertService.getStockAlertById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(stockAlertResponseDTO, result);
        verify(stockAlertRepository).findById(1L);
        verify(mapperStockAlert).toResponseDTO(stockAlertEntity);
    }

    @Test
    void getStockAlertById_NotFound() {
        // Arrange
        when(stockAlertRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> stockAlertService.getStockAlertById(1L)
        );
        assertEquals("Stock alert not found.", exception.getMessage());
        verify(stockAlertRepository).findById(1L);
        verify(mapperStockAlert, never()).toResponseDTO(any());
    }

    @Test
    void getAllStockAlerts_Success() {
        // Arrange
        List<StockAlertEntity> stockAlertEntities = Arrays.asList(stockAlertEntity);
        List<StockAlertResponseDTO> stockAlertResponseDTOs = Arrays.asList(stockAlertResponseDTO);

        when(stockAlertRepository.findAllSortedByEquipmentTypeLabel()).thenReturn(stockAlertEntities);
        when(mapperStockAlert.toStockAlertList(stockAlertEntities)).thenReturn(stockAlertResponseDTOs);

        // Act
        List<StockAlertResponseDTO> results = stockAlertService.getAllStockAlerts();

        // Assert
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals(stockAlertResponseDTO, results.get(0));
        verify(stockAlertRepository).findAllSortedByEquipmentTypeLabel();
        verify(mapperStockAlert).toStockAlertList(stockAlertEntities);
    }

    @Test
    void checkAndCreateAlert_ShouldCreateAlert() throws Exception {
        // Arrange
        when(stockAlertRepository.existsByStockAndStockAlertStatusNot(stockEntity, StockAlertStatus.RESOLVED)).thenReturn(false);
        when(stockAlertRepository.save(any(StockAlertEntity.class))).thenAnswer(invocation -> {
            StockAlertEntity savedEntity = invocation.getArgument(0);
            savedEntity.setId(1L);
            return savedEntity;
        });

        // Act
        stockAlertService.checkAndCreateAlert(stockEntity);

        // Assert
        verify(stockAlertRepository).existsByStockAndStockAlertStatusNot(stockEntity, StockAlertStatus.RESOLVED);
        verify(stockAlertRepository).save(any(StockAlertEntity.class));
        verify(emailStockAlertService).sendStockAlertEmail(any(StockAlertEntity.class));
    }

    @Test
    void checkAndCreateAlert_ShouldNotCreateAlert_StockAboveSecurityLevel() throws Exception {
        // Arrange
        StockEntity stockWithHighLevel = StockEntity.builder()
                .id(1L)
                .equipmentType(EquipmentType.NOTEBOOK)
                .currentStock(20)
                .minStock(10)
                .securityStock(15)
                .build();

        // Act
        stockAlertService.checkAndCreateAlert(stockWithHighLevel);

        // Assert
        verify(stockAlertRepository, never()).existsByStockAndStockAlertStatusNot(any(), any());
        verify(stockAlertRepository, never()).save(any());
        verify(emailStockAlertService, never()).sendStockAlertEmail(any());
    }

    @Test
    void checkAndCreateAlert_ShouldNotCreateAlert_AlertAlreadyExists() throws Exception {
        // Arrange
        when(stockAlertRepository.existsByStockAndStockAlertStatusNot(stockEntity, StockAlertStatus.RESOLVED)).thenReturn(true);

        // Act
        stockAlertService.checkAndCreateAlert(stockEntity);

        // Assert
        verify(stockAlertRepository).existsByStockAndStockAlertStatusNot(stockEntity, StockAlertStatus.RESOLVED);
        verify(stockAlertRepository, never()).save(any());
        verify(emailStockAlertService, never()).sendStockAlertEmail(any());
    }

    @Test
    void checkAndCreateAlert_EmailError() throws Exception {
        // Arrange
        when(stockAlertRepository.existsByStockAndStockAlertStatusNot(stockEntity, StockAlertStatus.RESOLVED)).thenReturn(false);
        when(stockAlertRepository.save(any(StockAlertEntity.class))).thenReturn(stockAlertEntity);
        doThrow(new Exception("Error sending stock alert email.")).when(emailStockAlertService).sendStockAlertEmail(any());

        // Act
        stockAlertService.checkAndCreateAlert(stockEntity);

        // Assert
        verify(stockAlertRepository).existsByStockAndStockAlertStatusNot(stockEntity, StockAlertStatus.RESOLVED);
        verify(stockAlertRepository).save(any(StockAlertEntity.class));
        verify(emailStockAlertService).sendStockAlertEmail(any());
        verify(i18nUtil).getMessage("stock.alert.email.error");
    }

    @Test
    void markAsProcessed_Success() {
        // Arrange
        when(stockAlertRepository.findById(1L)).thenReturn(Optional.of(stockAlertEntity));
        when(stockAlertRepository.save(stockAlertEntity)).thenReturn(stockAlertEntity);

        // Act
        stockAlertService.markAsProcessed(1L);

        // Assert
        verify(stockAlertRepository).findById(1L);
        verify(stockAlertRepository).save(stockAlertEntity);
        assertEquals(StockAlertStatus.PROCESSED, stockAlertEntity.getStockAlertStatus());
    }

    @Test
    void markAsProcessed_NotFound() {
        // Arrange
        when(stockAlertRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> stockAlertService.markAsProcessed(1L)
        );
        assertEquals("Stock alert not found.", exception.getMessage());
        verify(stockAlertRepository).findById(1L);
        verify(stockAlertRepository, never()).save(any());
    }

    @Test
    void markAsProcessed_InvalidStatus() {
        // Arrange
        StockAlertEntity processedAlert = StockAlertEntity.builder()
                .id(1L)
                .stock(stockEntity)
                .alertSentAt(now)
                .stockAlertStatus(StockAlertStatus.PROCESSED)
                .build();

        when(stockAlertRepository.findById(1L)).thenReturn(Optional.of(processedAlert));
        when(i18nUtil.getMessage("stock.alert.status.processed")).thenReturn("PROCESSED");

        // Act & Assert
        InvalidStatusException exception = assertThrows(
                InvalidStatusException.class,
                () -> stockAlertService.markAsProcessed(1L)
        );
        assertEquals("Stock alert with the status {0} can't be processed.", exception.getMessage());
        verify(stockAlertRepository).findById(1L);
        verify(stockAlertRepository, never()).save(any());
    }

    @Test
    void markAsResolved_Success() {
        // Arrange
        when(stockAlertRepository.findById(1L)).thenReturn(Optional.of(stockAlertEntity));
        when(stockRepository.findByEquipmentType(EquipmentType.NOTEBOOK)).thenReturn(stockEntity);
        when(stockAlertRepository.save(stockAlertEntity)).thenReturn(stockAlertEntity);

        stockEntity.setCurrentStock(15);

        // Act
        stockAlertService.markAsResolved(1L);

        // Assert
        verify(stockAlertRepository).findById(1L);
        verify(stockRepository).findByEquipmentType(EquipmentType.NOTEBOOK);
        verify(stockAlertRepository).save(stockAlertEntity);
        assertEquals(StockAlertStatus.RESOLVED, stockAlertEntity.getStockAlertStatus());
    }

    @Test
    void markAsResolved_NotFound() {
        // Arrange
        when(stockAlertRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> stockAlertService.markAsResolved(1L)
        );
        assertEquals("Stock alert not found.", exception.getMessage());
        verify(stockAlertRepository).findById(1L);
        verify(stockRepository, never()).findByEquipmentType(any());
        verify(stockAlertRepository, never()).save(any());
    }

    @Test
    void markAsResolved_AlreadyResolved() {
        // Arrange
        StockAlertEntity resolvedAlert = StockAlertEntity.builder()
                .id(1L)
                .stock(stockEntity)
                .alertSentAt(now)
                .stockAlertStatus(StockAlertStatus.RESOLVED)
                .build();

        when(stockAlertRepository.findById(1L)).thenReturn(Optional.of(resolvedAlert));

        // Act
        stockAlertService.markAsResolved(1L);

        // Assert
        verify(stockAlertRepository).findById(1L);
        verify(stockRepository, never()).findByEquipmentType(any());
        verify(stockAlertRepository, never()).save(any());
    }

    @Test
    void markAsResolved_StockNotFound() {
        // Arrange
        when(stockAlertRepository.findById(1L)).thenReturn(Optional.of(stockAlertEntity));
        when(stockRepository.findByEquipmentType(EquipmentType.NOTEBOOK)).thenReturn(null);

        // Act
        stockAlertService.markAsResolved(1L);

        // Assert
        verify(stockAlertRepository).findById(1L);
        verify(stockRepository).findByEquipmentType(EquipmentType.NOTEBOOK);
        verify(stockAlertRepository, never()).save(any());
    }

    @Test
    void markAsResolved_StockBelowMinimum() {
        // Arrange
        when(stockAlertRepository.findById(1L)).thenReturn(Optional.of(stockAlertEntity));
        when(stockRepository.findByEquipmentType(EquipmentType.NOTEBOOK)).thenReturn(stockEntity);

        stockEntity.setCurrentStock(5);
        stockEntity.setMinStock(10);

        // Act
        stockAlertService.markAsResolved(1L);

        // Assert
        verify(stockAlertRepository).findById(1L);
        verify(stockRepository).findByEquipmentType(EquipmentType.NOTEBOOK);
        verify(stockAlertRepository, never()).save(any());
        assertNotEquals(StockAlertStatus.RESOLVED, stockAlertEntity.getStockAlertStatus());
    }
}
package br.com.raroacademy.demo.controller;

import br.com.raroacademy.demo.commons.i18n.I18nUtil;
import br.com.raroacademy.demo.domain.DTO.stock.allert.StockAlertResponseDTO;
import br.com.raroacademy.demo.domain.enums.EquipmentType;
import br.com.raroacademy.demo.domain.enums.StockAlertStatus;
import br.com.raroacademy.demo.exception.InvalidStatusException;
import br.com.raroacademy.demo.exception.NotFoundException;
import br.com.raroacademy.demo.service.StockAlertService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class StockAlertControllerTest {

    @Mock
    private StockAlertService stockAlertService;

    @Mock
    private I18nUtil i18nUtil;

    @InjectMocks
    private StockAlertController stockAlertController;

    private StockAlertResponseDTO responseDTO;
    private List<StockAlertResponseDTO> responseDTOList;

    @BeforeEach
    void setUp() {
        // Set up test data
        responseDTO = StockAlertResponseDTO.builder()
                .id(1L)
                .equipmentType(EquipmentType.NOTEBOOK)
                .currentStock(5)
                .securityStock(10)
                .alertSentAt(Timestamp.from(Instant.now()))
                .stockAlertStatus(StockAlertStatus.CREATED)
                .build();

        responseDTOList = Arrays.asList(responseDTO);

        // Set up mock behaviors
        when(stockAlertService.getStockAlertById(anyLong())).thenReturn(responseDTO);
        when(stockAlertService.getAllStockAlerts()).thenReturn(responseDTOList);
        when(i18nUtil.getMessage("stock.alert.status.update")).thenReturn("Status updated to PROCESSED.");
    }

    @Test
    void getAllAlerts_Success() {
        // Act
        ResponseEntity<List<StockAlertResponseDTO>> response = stockAlertController.getAllAlerts();

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTOList, response.getBody());
        verify(stockAlertService).getAllStockAlerts();
    }

    @Test
    void getAlert_Success() {
        // Act
        ResponseEntity<StockAlertResponseDTO> response = stockAlertController.getAlert(1L);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
        verify(stockAlertService).getStockAlertById(1L);
    }

    @Test
    void getAlert_NotFound() {
        // Arrange
        when(stockAlertService.getStockAlertById(anyLong()))
                .thenThrow(new NotFoundException("Stock alert not found."));

        // Act & Assert
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> stockAlertController.getAlert(1L)
        );
        assertEquals("Stock alert not found.", exception.getMessage());
        verify(stockAlertService).getStockAlertById(1L);
    }

    @Test
    void processStockAlert_Success() {
        // Act
        ResponseEntity<String> response = stockAlertController.processStockAlert(1L);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Status updated to PROCESSED.", response.getBody());
        verify(stockAlertService).markAsProcessed(1L);
        verify(i18nUtil).getMessage("stock.alert.status.update");
    }

    @Test
    void processStockAlert_NotFound() {
        // Arrange
        doThrow(new NotFoundException("Stock alert not found."))
                .when(stockAlertService).markAsProcessed(anyLong());

        // Act & Assert
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> stockAlertController.processStockAlert(1L)
        );
        assertEquals("Stock alert not found.", exception.getMessage());
        verify(stockAlertService).markAsProcessed(1L);
    }

    @Test
    void processStockAlert_InvalidStatus() {
        // Arrange
        doThrow(new InvalidStatusException("Stock alert with the status {0} can't be processed."))
                .when(stockAlertService).markAsProcessed(anyLong());

        // Act & Assert
        InvalidStatusException exception = assertThrows(
                InvalidStatusException.class,
                () -> stockAlertController.processStockAlert(1L)
        );
        assertEquals("Stock alert with the status {0} can't be processed.", exception.getMessage());
        verify(stockAlertService).markAsProcessed(1L);
    }
}
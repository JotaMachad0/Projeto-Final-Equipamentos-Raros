package br.com.raroacademy.demo.controller;

import br.com.raroacademy.demo.commons.i18n.I18nUtil;
import br.com.raroacademy.demo.domain.DTO.stock.StockRequestDTO;
import br.com.raroacademy.demo.domain.DTO.stock.StockResponseDTO;
import br.com.raroacademy.demo.domain.enums.EquipmentType;
import br.com.raroacademy.demo.exception.NotFoundException;
import br.com.raroacademy.demo.service.StockService;
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

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class StockControllerTest {

    @Mock
    private StockService stockService;

    @Mock
    private I18nUtil i18n;

    @InjectMocks
    private StockController stockController;

    private StockRequestDTO requestDTO;
    private StockResponseDTO responseDTO;
    private List<StockResponseDTO> responseDTOList;

    @BeforeEach
    void setUp() {

        requestDTO = new StockRequestDTO(
                10,
                15
        );

        responseDTO = StockResponseDTO.builder()
                .id(1L)
                .equipmentType(EquipmentType.NOTEBOOK)
                .minStock(10)
                .securityStock(15)
                .currentStock(20)
                .avgRestockTimeDays(7)
                .avgStockConsumptionTimeDays(30)
                .avgDefectiveRate(0.05f)
                .build();

        responseDTOList = Arrays.asList(responseDTO);

        when(stockService.getAllStocks()).thenReturn(responseDTOList);
        when(stockService.findByEquipmentType(any(EquipmentType.class))).thenReturn(responseDTO);
        when(stockService.updateByEquipmentType(any(EquipmentType.class), any(StockRequestDTO.class))).thenReturn(responseDTO);
    }

    @Test
    void getAll_Success() {
        // Act
        ResponseEntity<List<StockResponseDTO>> response = stockController.getAll();

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTOList, response.getBody());
        verify(stockService).getAllStocks();
    }

    @Test
    void getByType_Success() {
        // Act
        ResponseEntity<StockResponseDTO> response = stockController.getByType(EquipmentType.NOTEBOOK);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
        verify(stockService).findByEquipmentType(EquipmentType.NOTEBOOK);
    }

    @Test
    void getByType_NotFound() {
        // Arrange
        when(stockService.findByEquipmentType(any(EquipmentType.class)))
                .thenThrow(new IllegalStateException("Stock not found for type NOTEBOOK"));

        // Act & Assert
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> stockController.getByType(EquipmentType.NOTEBOOK)
        );
        assertEquals("Stock not found for type NOTEBOOK", exception.getMessage());
        verify(stockService).findByEquipmentType(EquipmentType.NOTEBOOK);
    }

    @Test
    void update_Success() {
        // Act
        ResponseEntity<StockResponseDTO> response = stockController.update(EquipmentType.NOTEBOOK, requestDTO);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
        verify(stockService).updateByEquipmentType(EquipmentType.NOTEBOOK, requestDTO);
    }

    @Test
    void update_NotFound() {
        // Arrange
        when(stockService.updateByEquipmentType(any(EquipmentType.class), any(StockRequestDTO.class)))
                .thenThrow(new NotFoundException("Stock not found for type NOTEBOOK"));

        // Act & Assert
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> stockController.update(EquipmentType.NOTEBOOK, requestDTO)
        );
        assertEquals("Stock not found for type NOTEBOOK", exception.getMessage());
        verify(stockService).updateByEquipmentType(EquipmentType.NOTEBOOK, requestDTO);
    }
}
package br.com.raroacademy.demo.controller;

import br.com.raroacademy.demo.domain.DTO.equipment.purchase.EquipmentPurchasesRequestDTO;
import br.com.raroacademy.demo.domain.DTO.equipment.purchase.EquipmentPurchasesResponseDTO;
import br.com.raroacademy.demo.domain.enums.EquipmentType;
import br.com.raroacademy.demo.domain.enums.PurchaseStatus;
import br.com.raroacademy.demo.exception.NotFoundException;
import br.com.raroacademy.demo.service.EquipmentPurchasesService;
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

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class EquipmentPurchasesControllerTest {

    @Mock
    private EquipmentPurchasesService equipmentPurchasesService;

    @InjectMocks
    private EquipmentPurchasesController equipmentPurchasesController;

    private EquipmentPurchasesRequestDTO requestDTO;
    private EquipmentPurchasesResponseDTO responseDTO;
    private List<EquipmentPurchasesResponseDTO> responseDTOList;

    @BeforeEach
    void setUp() {
        LocalDate orderDate = LocalDate.now();
        LocalDate receiptDate = LocalDate.now().plusDays(10);

        requestDTO = EquipmentPurchasesRequestDTO.builder()
                .equipmentType(EquipmentType.NOTEBOOK)
                .quantity(10)
                .orderDate(orderDate)
                .supplier("Tech Supplies Inc.")
                .receiptDate(null)
                .status(PurchaseStatus.PURCHASED)
                .build();

        responseDTO = EquipmentPurchasesResponseDTO.builder()
                .id(1L)
                .equipmentType(EquipmentType.NOTEBOOK)
                .quantity(10)
                .orderDate(orderDate)
                .supplier("Tech Supplies Inc.")
                .receiptDate(null)
                .status(PurchaseStatus.PURCHASED)
                .build();

        responseDTOList = Arrays.asList(responseDTO);

        when(equipmentPurchasesService.create(any(EquipmentPurchasesRequestDTO.class))).thenReturn(responseDTO);
        when(equipmentPurchasesService.getById(anyLong())).thenReturn(responseDTO);
        when(equipmentPurchasesService.update(anyLong(), any(EquipmentPurchasesRequestDTO.class))).thenReturn(responseDTO);
        when(equipmentPurchasesService.getAll()).thenReturn(responseDTOList);
        when(equipmentPurchasesService.registerInStock(anyLong())).thenReturn(responseDTO);
    }

    @Test
    void createPurchase_Success() {
        // Act
        ResponseEntity<EquipmentPurchasesResponseDTO> response = equipmentPurchasesController.createPurchase(requestDTO);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
        verify(equipmentPurchasesService).create(requestDTO);
    }

    @Test
    void getPurchaseById_Success() {
        // Act
        ResponseEntity<EquipmentPurchasesResponseDTO> response = equipmentPurchasesController.getPurchaseById(1L);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
        verify(equipmentPurchasesService).getById(1L);
    }

    @Test
    void getPurchaseById_NotFound() {
        // Arrange
        when(equipmentPurchasesService.getById(anyLong()))
                .thenThrow(new NotFoundException("Purchase not found"));

        // Act & Assert
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> equipmentPurchasesController.getPurchaseById(1L)
        );
        assertEquals("Purchase not found", exception.getMessage());
        verify(equipmentPurchasesService).getById(1L);
    }

    @Test
    void getAllPurchases_Success() {
        // Act
        ResponseEntity<List<EquipmentPurchasesResponseDTO>> response = equipmentPurchasesController.getAllPurchases();

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTOList, response.getBody());
        verify(equipmentPurchasesService).getAll();
    }

    @Test
    void updatePurchase_Success() {
        // Act
        ResponseEntity<EquipmentPurchasesResponseDTO> response = equipmentPurchasesController.updatePurchase(1L, requestDTO);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
        verify(equipmentPurchasesService).update(1L, requestDTO);
    }

    @Test
    void updatePurchase_NotFound() {
        // Arrange
        when(equipmentPurchasesService.update(anyLong(), any(EquipmentPurchasesRequestDTO.class)))
                .thenThrow(new NotFoundException("Purchase not found"));

        // Act & Assert
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> equipmentPurchasesController.updatePurchase(1L, requestDTO)
        );
        assertEquals("Purchase not found", exception.getMessage());
        verify(equipmentPurchasesService).update(1L, requestDTO);
    }

    @Test
    void registerInStock_Success() {
        // Act
        ResponseEntity<EquipmentPurchasesResponseDTO> response = equipmentPurchasesController.registerInStock(1L);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
        verify(equipmentPurchasesService).registerInStock(1L);
    }

    @Test
    void registerInStock_NotFound() {
        // Arrange
        when(equipmentPurchasesService.registerInStock(anyLong()))
                .thenThrow(new NotFoundException("Purchase not found"));

        // Act & Assert
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> equipmentPurchasesController.registerInStock(1L)
        );
        assertEquals("Purchase not found", exception.getMessage());
        verify(equipmentPurchasesService).registerInStock(1L);
    }
}
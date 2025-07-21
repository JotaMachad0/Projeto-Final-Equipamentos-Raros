package br.com.raroacademy.demo.controller;

import br.com.raroacademy.demo.domain.DTO.equipment.EquipmentRequestDTO;
import br.com.raroacademy.demo.domain.DTO.equipment.EquipmentResponseDTO;
import br.com.raroacademy.demo.domain.enums.EquipmentStatus;
import br.com.raroacademy.demo.exception.NotFoundException;
import br.com.raroacademy.demo.service.EquipmentService;
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
public class EquipmentControllerTest {

    @Mock
    private EquipmentService equipmentService;

    @InjectMocks
    private EquipmentController equipmentController;

    private EquipmentRequestDTO requestDTO;
    private EquipmentResponseDTO responseDTO;
    private List<EquipmentResponseDTO> responseDTOList;

    @BeforeEach
    void setUp() {
        LocalDate acquisitionDate = LocalDate.now().minusMonths(6);

        requestDTO = new EquipmentRequestDTO(
                "NOTEBOOK",
                "SN123456789",
                "Dell",
                "XPS 13",
                "16GB RAM, 512GB SSD",
                acquisitionDate,
                24,
                EquipmentStatus.AVAILABLE
        );

        responseDTO = EquipmentResponseDTO.builder()
                .id(1L)
                .type("NOTEBOOK")
                .serialNumber("SN123456789")
                .brand("Dell")
                .model("XPS 13")
                .specs("16GB RAM, 512GB SSD")
                .acquisitionDate(acquisitionDate)
                .usageTimeMonths(24)
                .status(EquipmentStatus.AVAILABLE)
                .build();

        responseDTOList = Arrays.asList(responseDTO);

        when(equipmentService.create(any(EquipmentRequestDTO.class))).thenReturn(responseDTO);
        when(equipmentService.getEquipment(anyLong())).thenReturn(responseDTO);
        when(equipmentService.update(anyLong(), any(EquipmentRequestDTO.class))).thenReturn(responseDTO);
        when(equipmentService.getAll()).thenReturn(responseDTOList);
    }

    @Test
    void create_Success() {
        // Act
        ResponseEntity<EquipmentResponseDTO> response = equipmentController.create(requestDTO);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
        verify(equipmentService).create(requestDTO);
    }

    @Test
    void create_DuplicateSerialNumber() {
        // Arrange
        when(equipmentService.create(any(EquipmentRequestDTO.class)))
                .thenThrow(new br.com.raroacademy.demo.exception.DataIntegrityException("Serial number already exists"));

        // Act & Assert
        br.com.raroacademy.demo.exception.DataIntegrityException exception = assertThrows(
                br.com.raroacademy.demo.exception.DataIntegrityException.class,
                () -> equipmentController.create(requestDTO)
        );
        assertEquals("Serial number already exists", exception.getMessage());
        verify(equipmentService).create(requestDTO);
    }

    @Test
    void getEquipment_Success() {
        // Act
        ResponseEntity<EquipmentResponseDTO> response = equipmentController.getEquipment(1L);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
        verify(equipmentService).getEquipment(1L);
    }

    @Test
    void getEquipment_NotFound() {
        // Arrange
        when(equipmentService.getEquipment(anyLong()))
                .thenThrow(new NotFoundException("Equipment not found"));

        // Act & Assert
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> equipmentController.getEquipment(1L)
        );
        assertEquals("Equipment not found", exception.getMessage());
        verify(equipmentService).getEquipment(1L);
    }

    @Test
    void getAll_Success() {
        // Act
        ResponseEntity<List<EquipmentResponseDTO>> response = equipmentController.getAll();

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTOList, response.getBody());
        verify(equipmentService).getAll();
    }

    @Test
    void updateEquipment_Success() {
        // Act
        ResponseEntity<EquipmentResponseDTO> response = equipmentController.updateEquipment(1L, requestDTO);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
        verify(equipmentService).update(1L, requestDTO);
    }

    @Test
    void updateEquipment_NotFound() {
        // Arrange
        when(equipmentService.update(anyLong(), any(EquipmentRequestDTO.class)))
                .thenThrow(new NotFoundException("Equipment not found"));

        // Act & Assert
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> equipmentController.updateEquipment(1L, requestDTO)
        );
        assertEquals("Equipment not found", exception.getMessage());
        verify(equipmentService).update(1L, requestDTO);
    }

    @Test
    void updateEquipment_DuplicateSerialNumber() {
        // Arrange
        when(equipmentService.update(anyLong(), any(EquipmentRequestDTO.class)))
                .thenThrow(new br.com.raroacademy.demo.exception.DataIntegrityException("Serial number already exists"));

        // Act & Assert
        br.com.raroacademy.demo.exception.DataIntegrityException exception = assertThrows(
                br.com.raroacademy.demo.exception.DataIntegrityException.class,
                () -> equipmentController.updateEquipment(1L, requestDTO)
        );
        assertEquals("Serial number already exists", exception.getMessage());
        verify(equipmentService).update(1L, requestDTO);
    }

    @Test
    void deleteEquipment_Success() {
        // Act
        ResponseEntity<Void> response = equipmentController.deleteEquipment(1L);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(equipmentService).delete(1L);
    }

    @Test
    void deleteEquipment_NotFound() {
        // Arrange
        doThrow(new NotFoundException("Equipment not found"))
                .when(equipmentService).delete(anyLong());

        // Act & Assert
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> equipmentController.deleteEquipment(1L)
        );
        assertEquals("Equipment not found", exception.getMessage());
        verify(equipmentService).delete(1L);
    }
}
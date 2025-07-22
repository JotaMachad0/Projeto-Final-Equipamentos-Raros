package br.com.raroacademy.demo.controller;

import br.com.raroacademy.demo.domain.DTO.collaborator.CollaboratorSummaryDTO;
import br.com.raroacademy.demo.domain.DTO.equipment.EquipmentSummaryDTO;
import br.com.raroacademy.demo.domain.DTO.equipment.collaborator.EquipmentCollaboratorRequestDTO;
import br.com.raroacademy.demo.domain.DTO.equipment.collaborator.EquipmentCollaboratorResponseDTO;
import br.com.raroacademy.demo.domain.DTO.equipment.collaborator.NewCollaboratorEquipmentLinkRequestDTO;
import br.com.raroacademy.demo.exception.NotFoundException;
import br.com.raroacademy.demo.service.EquipmentCollaboratorService;
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
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class EquipmentCollaboratorControllerTest {

    @Mock
    private EquipmentCollaboratorService equipmentCollaboratorService;

    @InjectMocks
    private EquipmentCollaboratorController equipmentCollaboratorController;

    private NewCollaboratorEquipmentLinkRequestDTO createRequestDTO;
    private EquipmentCollaboratorRequestDTO updateRequestDTO;
    private EquipmentCollaboratorResponseDTO responseDTO;
    private List<EquipmentCollaboratorResponseDTO> responseDTOList;

    @BeforeEach
    void setUp() {
        LocalDate today = LocalDate.now();

        createRequestDTO = new NewCollaboratorEquipmentLinkRequestDTO(
                1L,
                1L,
                today,
                "SHIPPED",
                "Test notes for creation"
        );

        updateRequestDTO = new EquipmentCollaboratorRequestDTO(
                1L,
                1L,
                today,
                today.plusMonths(12),
                "Delivered",
                "Test notes for update"
        );

        CollaboratorSummaryDTO collaboratorSummaryDTO = CollaboratorSummaryDTO.builder()
                .id(1L)
                .name("Test Collaborator")
                .cpf("12345678901")
                .email("test@example.com")
                .build();

        EquipmentSummaryDTO equipmentSummaryDTO = EquipmentSummaryDTO.builder()
                .id(1L)
                .type("Notebook")
                .model("Test Model")
                .build();

        responseDTO = new EquipmentCollaboratorResponseDTO(
                1L,
                today,
                today.plusDays(5),
                today.plusMonths(12),
                "Delivered",
                "Test notes",
                collaboratorSummaryDTO,
                equipmentSummaryDTO
        );

        responseDTOList = Collections.singletonList(responseDTO);

        when(equipmentCollaboratorService.create(any(NewCollaboratorEquipmentLinkRequestDTO.class))).thenReturn(responseDTO);
        when(equipmentCollaboratorService.getById(anyLong())).thenReturn(responseDTO);
        when(equipmentCollaboratorService.update(anyLong(), any(EquipmentCollaboratorRequestDTO.class))).thenReturn(responseDTO);
        when(equipmentCollaboratorService.getAll()).thenReturn(responseDTOList);
    }

    @Test
    void create_Success() {
        // Act
        ResponseEntity<EquipmentCollaboratorResponseDTO> response = equipmentCollaboratorController.create(createRequestDTO);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
        verify(equipmentCollaboratorService).create(createRequestDTO);
    }

    @Test
    void create_CollaboratorNotFound() {
        // Arrange
        when(equipmentCollaboratorService.create(any(NewCollaboratorEquipmentLinkRequestDTO.class)))
                .thenThrow(new NotFoundException("Collaborator not found"));

        // Act & Assert
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> equipmentCollaboratorController.create(createRequestDTO)
        );
        assertEquals("Collaborator not found", exception.getMessage());
        verify(equipmentCollaboratorService).create(createRequestDTO);
    }

    @Test
    void create_EquipmentNotFound() {
        // Arrange
        when(equipmentCollaboratorService.create(any(NewCollaboratorEquipmentLinkRequestDTO.class)))
                .thenThrow(new NotFoundException("Equipment not found"));

        // Act & Assert
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> equipmentCollaboratorController.create(createRequestDTO)
        );
        assertEquals("Equipment not found", exception.getMessage());
        verify(equipmentCollaboratorService).create(createRequestDTO);
    }

    @Test
    void getById_Success() {
        // Act
        ResponseEntity<EquipmentCollaboratorResponseDTO> response = equipmentCollaboratorController.getById(1L);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
        verify(equipmentCollaboratorService).getById(1L);
    }

    @Test
    void getById_NotFound() {
        // Arrange
        when(equipmentCollaboratorService.getById(anyLong()))
                .thenThrow(new NotFoundException("Equipment-Collaborator relationship not found"));

        // Act & Assert
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> equipmentCollaboratorController.getById(1L)
        );
        assertEquals("Equipment-Collaborator relationship not found", exception.getMessage());
        verify(equipmentCollaboratorService).getById(1L);
    }

    @Test
    void getAll_Success() {
        // Act
        ResponseEntity<List<EquipmentCollaboratorResponseDTO>> response = equipmentCollaboratorController.getAll();

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTOList, response.getBody());
        verify(equipmentCollaboratorService).getAll();
    }

    @Test
    void update_Success() {
        // Act
        ResponseEntity<EquipmentCollaboratorResponseDTO> response = equipmentCollaboratorController.update(1L, updateRequestDTO);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
        verify(equipmentCollaboratorService).update(1L, updateRequestDTO);
    }

    @Test
    void update_NotFound() {
        // Arrange
        when(equipmentCollaboratorService.update(anyLong(), any(EquipmentCollaboratorRequestDTO.class)))
                .thenThrow(new NotFoundException("Equipment-Collaborator relationship not found"));

        // Act & Assert
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> equipmentCollaboratorController.update(1L, updateRequestDTO)
        );
        assertEquals("Equipment-Collaborator relationship not found", exception.getMessage());
        verify(equipmentCollaboratorService).update(1L, updateRequestDTO);
    }

    @Test
    void delete_Success() {
        // Act
        ResponseEntity<Void> response = equipmentCollaboratorController.delete(1L);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(equipmentCollaboratorService).delete(1L);
    }

    @Test
    void delete_NotFound() {
        // Arrange
        doThrow(new NotFoundException("Equipment-Collaborator relationship not found"))
                .when(equipmentCollaboratorService).delete(anyLong());

        // Act & Assert
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> equipmentCollaboratorController.delete(1L)
        );
        assertEquals("Equipment-Collaborator relationship not found", exception.getMessage());
        verify(equipmentCollaboratorService).delete(1L);
    }
}
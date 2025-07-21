package br.com.raroacademy.demo.controller;

import br.com.raroacademy.demo.domain.DTO.address.AddressResponseDTO;
import br.com.raroacademy.demo.domain.DTO.collaborator.CollaboratorRequestDTO;
import br.com.raroacademy.demo.domain.DTO.collaborator.CollaboratorResponseDTO;
import br.com.raroacademy.demo.exception.InvalidCepException;
import br.com.raroacademy.demo.exception.NotFoundException;
import br.com.raroacademy.demo.service.CollaboratorService;
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
public class CollaboratorControllerTest {

    @Mock
    private CollaboratorService collaboratorService;

    @InjectMocks
    private CollaboratorController collaboratorController;

    private CollaboratorRequestDTO collaboratorRequestDTO;
    private CollaboratorResponseDTO collaboratorResponseDTO;
    private List<CollaboratorResponseDTO> collaboratorResponseDTOList;

    @BeforeEach
    void setUp() {
        collaboratorRequestDTO = CollaboratorRequestDTO.builder()
                .name("Test Collaborator")
                .cpf("12345678901")
                .email("test@example.com")
                .phone("1234567890")
                .addressId(1L)
                .contractStartDate(LocalDate.now())
                .contractEndDate(LocalDate.now().plusYears(1))
                .cep("12345678")
                .number("123")
                .complement("Apt 101")
                .build();

        AddressResponseDTO addressResponseDTO = AddressResponseDTO.builder()
                .id(1L)
                .cep("12345678")
                .street("Test Street")
                .number("123")
                .complement("Apt 101")
                .neighborhood("Test Neighborhood")
                .city("Test City")
                .state("TS")
                .country("Brazil")
                .build();

        collaboratorResponseDTO = CollaboratorResponseDTO.builder()
                .id(1L)
                .name("Test Collaborator")
                .cpf("12345678901")
                .email("test@example.com")
                .phone("1234567890")
                .contractStartDate(LocalDate.now())
                .contractEndDate(LocalDate.now().plusYears(1))
                .address(addressResponseDTO)
                .build();

        collaboratorResponseDTOList = Arrays.asList(collaboratorResponseDTO);

        when(collaboratorService.save(any(CollaboratorRequestDTO.class))).thenReturn(collaboratorResponseDTO);
        when(collaboratorService.getById(anyLong())).thenReturn(collaboratorResponseDTO);
        when(collaboratorService.update(anyLong(), any(CollaboratorRequestDTO.class))).thenReturn(collaboratorResponseDTO);
        when(collaboratorService.getAll()).thenReturn(collaboratorResponseDTOList);
    }

    @Test
    void createCollaborator_Success() {
        // Act
        ResponseEntity<CollaboratorResponseDTO> response = collaboratorController.createCollaborator(collaboratorRequestDTO);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(collaboratorResponseDTO, response.getBody());
        verify(collaboratorService).save(collaboratorRequestDTO);
    }

    @Test
    void createCollaborator_DuplicateCpf() {
        // Arrange
        when(collaboratorService.save(any(CollaboratorRequestDTO.class)))
                .thenThrow(new br.com.raroacademy.demo.exception.DataIntegrityException("CPF already exists"));

        // Act & Assert
        br.com.raroacademy.demo.exception.DataIntegrityException exception = assertThrows(
                br.com.raroacademy.demo.exception.DataIntegrityException.class,
                () -> collaboratorController.createCollaborator(collaboratorRequestDTO)
        );
        assertEquals("CPF already exists", exception.getMessage());
        verify(collaboratorService).save(collaboratorRequestDTO);
    }

    @Test
    void createCollaborator_InvalidCep() {
        // Arrange
        when(collaboratorService.save(any(CollaboratorRequestDTO.class)))
                .thenThrow(new InvalidCepException("Invalid CEP"));

        // Act & Assert
        InvalidCepException exception = assertThrows(
                InvalidCepException.class,
                () -> collaboratorController.createCollaborator(collaboratorRequestDTO)
        );
        assertEquals("Invalid CEP", exception.getMessage());
        verify(collaboratorService).save(collaboratorRequestDTO);
    }

    @Test
    void getCollaborator_Success() {
        // Act
        ResponseEntity<CollaboratorResponseDTO> response = collaboratorController.getCollaborator(1L);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(collaboratorResponseDTO, response.getBody());
        verify(collaboratorService).getById(1L);
    }

    @Test
    void getCollaborator_NotFound() {
        // Arrange
        when(collaboratorService.getById(anyLong()))
                .thenThrow(new NotFoundException("Collaborator not found"));

        // Act & Assert
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> collaboratorController.getCollaborator(1L)
        );
        assertEquals("Collaborator not found", exception.getMessage());
        verify(collaboratorService).getById(1L);
    }

    @Test
    void getAllCollaborators_Success() {
        // Act
        ResponseEntity<List<CollaboratorResponseDTO>> response = collaboratorController.getAllCollaborators();

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(collaboratorResponseDTOList, response.getBody());
        verify(collaboratorService).getAll();
    }

    @Test
    void updateCollaborator_Success() {
        // Act
        ResponseEntity<CollaboratorResponseDTO> response = collaboratorController.updateCollaborator(1L, collaboratorRequestDTO);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(collaboratorResponseDTO, response.getBody());
        verify(collaboratorService).update(1L, collaboratorRequestDTO);
    }

    @Test
    void updateCollaborator_NotFound() {
        // Arrange
        when(collaboratorService.update(anyLong(), any(CollaboratorRequestDTO.class)))
                .thenThrow(new NotFoundException("Collaborator not found"));

        // Act & Assert
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> collaboratorController.updateCollaborator(1L, collaboratorRequestDTO)
        );
        assertEquals("Collaborator not found", exception.getMessage());
        verify(collaboratorService).update(1L, collaboratorRequestDTO);
    }

    @Test
    void updateCollaborator_InvalidCep() {
        // Arrange
        when(collaboratorService.update(anyLong(), any(CollaboratorRequestDTO.class)))
                .thenThrow(new InvalidCepException("Invalid CEP"));

        // Act & Assert
        InvalidCepException exception = assertThrows(
                InvalidCepException.class,
                () -> collaboratorController.updateCollaborator(1L, collaboratorRequestDTO)
        );
        assertEquals("Invalid CEP", exception.getMessage());
        verify(collaboratorService).update(1L, collaboratorRequestDTO);
    }

    @Test
    void deleteCollaborator_Success() {
        // Act
        ResponseEntity<Void> response = collaboratorController.deleteCollaborator(1L);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(collaboratorService).delete(1L);
    }

    @Test
    void deleteCollaborator_NotFound() {
        // Arrange
        doThrow(new NotFoundException("Collaborator not found"))
                .when(collaboratorService).delete(anyLong());

        // Act & Assert
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> collaboratorController.deleteCollaborator(1L)
        );
        assertEquals("Collaborator not found", exception.getMessage());
        verify(collaboratorService).delete(1L);
    }
}
package br.com.raroacademy.demo.controller;

import br.com.raroacademy.demo.commons.i18n.I18nUtil;
import br.com.raroacademy.demo.domain.DTO.expected.hirings.ExpectedHiringRequestDTO;
import br.com.raroacademy.demo.domain.DTO.expected.hirings.ExpectedHiringResponseDTO;
import br.com.raroacademy.demo.domain.enums.ExpectedHiringStatus;
import br.com.raroacademy.demo.domain.enums.Region;
import br.com.raroacademy.demo.exception.NotFoundException;
import br.com.raroacademy.demo.service.ExpectedHiringService;
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
public class ExpectedHiringControllerTest {

    @Mock
    private ExpectedHiringService expectedHiringService;

    @Mock
    private I18nUtil i18nUtil;

    @InjectMocks
    private ExpectedHiringController expectedHiringController;

    private ExpectedHiringRequestDTO requestDTO;
    private ExpectedHiringResponseDTO responseDTO;
    private List<ExpectedHiringResponseDTO> responseDTOList;

    @BeforeEach
    void setUp() {
        LocalDate futureDate = LocalDate.now().plusMonths(3);

        requestDTO = new ExpectedHiringRequestDTO(
                futureDate,
                "Developer",
                "2 cellphones, 1 16GB notebook",
                Region.SUDESTE
        );

        responseDTO = ExpectedHiringResponseDTO.builder()
                .id(1L)
                .expectedHireDate(futureDate)
                .position("Developer")
                .equipmentRequirements("2 cellphones, 1 16GB notebook")
                .region(Region.SUDESTE)
                .expectedHiringStatus(ExpectedHiringStatus.CREATED)
                .build();

        responseDTOList = Arrays.asList(responseDTO);

        when(expectedHiringService.create(any(ExpectedHiringRequestDTO.class))).thenReturn(responseDTO);
        when(expectedHiringService.getExpectedHiringById(anyLong())).thenReturn(responseDTO);
        when(expectedHiringService.update(anyLong(), any(ExpectedHiringRequestDTO.class))).thenReturn(responseDTO);
        when(expectedHiringService.getAllExpectedHirings()).thenReturn(responseDTOList);
        when(i18nUtil.getMessage("expected.hiring.status.update")).thenReturn("Status updated to PROCESSED.");
    }

    @Test
    void createExpectedHiring_Success() {
        // Act
        ResponseEntity<ExpectedHiringResponseDTO> response = expectedHiringController.createExpectedHiring(requestDTO);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
        verify(expectedHiringService).create(requestDTO);
    }

    @Test
    void getExpectedHiring_Success() {
        // Act
        ResponseEntity<ExpectedHiringResponseDTO> response = expectedHiringController.getExpectedHiring(1L);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
        verify(expectedHiringService).getExpectedHiringById(1L);
    }

    @Test
    void getExpectedHiring_NotFound() {
        // Arrange
        when(expectedHiringService.getExpectedHiringById(anyLong()))
                .thenThrow(new NotFoundException("Expected hiring not found."));

        // Act & Assert
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> expectedHiringController.getExpectedHiring(1L)
        );
        assertEquals("Expected hiring not found.", exception.getMessage());
        verify(expectedHiringService).getExpectedHiringById(1L);
    }

    @Test
    void updateExpectedHiring_Success() {
        // Act
        ResponseEntity<ExpectedHiringResponseDTO> response = expectedHiringController.updateExpectedHiring(1L, requestDTO);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
        verify(expectedHiringService).update(1L, requestDTO);
    }

    @Test
    void updateExpectedHiring_NotFound() {
        // Arrange
        when(expectedHiringService.update(anyLong(), any(ExpectedHiringRequestDTO.class)))
                .thenThrow(new NotFoundException("Expected hiring not found."));

        // Act & Assert
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> expectedHiringController.updateExpectedHiring(1L, requestDTO)
        );
        assertEquals("Expected hiring not found.", exception.getMessage());
        verify(expectedHiringService).update(1L, requestDTO);
    }

    @Test
    void deleteExpectedHiring_Success() {
        // Act
        ResponseEntity<Void> response = expectedHiringController.deleteExpectedHiring(1L);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(expectedHiringService).delete(1L);
    }

    @Test
    void deleteExpectedHiring_NotFound() {
        // Arrange
        doThrow(new NotFoundException("Expected hiring not found."))
                .when(expectedHiringService).delete(anyLong());

        // Act & Assert
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> expectedHiringController.deleteExpectedHiring(1L)
        );
        assertEquals("Expected hiring not found.", exception.getMessage());
        verify(expectedHiringService).delete(1L);
    }

    @Test
    void getAllExpectedHirings_Success() {
        // Act
        ResponseEntity<List<ExpectedHiringResponseDTO>> response = expectedHiringController.getAllExpectedHirings();

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTOList, response.getBody());
        verify(expectedHiringService).getAllExpectedHirings();
    }

    @Test
    void startHiring_Success() {
        // Act
        ResponseEntity<String> response = expectedHiringController.startHiring(1L);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Status updated to PROCESSED.", response.getBody());
        verify(expectedHiringService).markAsProcessed(1L);
        verify(i18nUtil).getMessage("expected.hiring.status.update");
    }

    @Test
    void startHiring_NotFound() {
        // Arrange
        doThrow(new NotFoundException("Expected hiring not found."))
                .when(expectedHiringService).markAsProcessed(anyLong());

        // Act & Assert
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> expectedHiringController.startHiring(1L)
        );
        assertEquals("Expected hiring not found.", exception.getMessage());
        verify(expectedHiringService).markAsProcessed(1L);
    }
}
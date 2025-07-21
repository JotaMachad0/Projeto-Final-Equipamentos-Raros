package br.com.raroacademy.demo.controller;

import br.com.raroacademy.demo.domain.DTO.equipment.returns.CreateReturnScheduleRequestDTO;
import br.com.raroacademy.demo.domain.DTO.equipment.returns.EquipmentReturnResponseDTO;
import br.com.raroacademy.demo.domain.DTO.equipment.returns.ProcessReturnRequestDTO;
import br.com.raroacademy.demo.domain.enums.EquipmentStatus;
import br.com.raroacademy.demo.domain.enums.ReturnStatus;
import br.com.raroacademy.demo.exception.BusinessException;
import br.com.raroacademy.demo.exception.NotFoundException;
import br.com.raroacademy.demo.service.EquipmentReturnService;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class EquipmentReturnControllerTest {

    @Mock
    private EquipmentReturnService equipmentReturnService;

    @InjectMocks
    private EquipmentReturnController equipmentReturnController;

    private CreateReturnScheduleRequestDTO createScheduleRequestDTO;
    private ProcessReturnRequestDTO processReturnRequestDTO;
    private EquipmentReturnResponseDTO responseDTO;
    private List<EquipmentReturnResponseDTO> responseDTOList;

    @BeforeEach
    void setUp() {
        createScheduleRequestDTO = new CreateReturnScheduleRequestDTO(LocalDate.now(), 1L);

        processReturnRequestDTO = new ProcessReturnRequestDTO(LocalDate.now(), EquipmentStatus.AVAILABLE, "Equipment returned in good condition");

        responseDTO = EquipmentReturnResponseDTO.builder()
                .id(1L)
                .status(ReturnStatus.PENDING)
                .deliveryDate(LocalDate.now())
                .build();

        responseDTOList = Collections.singletonList(responseDTO);

        when(equipmentReturnService.createSchedule(any(CreateReturnScheduleRequestDTO.class))).thenReturn(responseDTO);
        when(equipmentReturnService.processReturn(anyLong(), any(ProcessReturnRequestDTO.class))).thenReturn(responseDTO);
        when(equipmentReturnService.getAll()).thenReturn(responseDTOList);
    }

    @Test
    void createSchedule_Success() {
        // Act
        ResponseEntity<EquipmentReturnResponseDTO> response = equipmentReturnController.createSchedule(createScheduleRequestDTO);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
        verify(equipmentReturnService).createSchedule(createScheduleRequestDTO);
    }

    @Test
    void createSchedule_Failure_AlreadyExists() {
        // Arrange
        when(equipmentReturnService.createSchedule(any(CreateReturnScheduleRequestDTO.class)))
                .thenThrow(new BusinessException("Pending return already exists for this item."));

        // Act & Assert
        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> equipmentReturnController.createSchedule(createScheduleRequestDTO)
        );
        assertEquals("Pending return already exists for this item.", exception.getMessage());
        verify(equipmentReturnService).createSchedule(createScheduleRequestDTO);
    }

    @Test
    void processReturn_Success() {
        // Act
        ResponseEntity<EquipmentReturnResponseDTO> response = equipmentReturnController.processReturn(1L, processReturnRequestDTO);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
        verify(equipmentReturnService).processReturn(1L, processReturnRequestDTO);
    }

    @Test
    void processReturn_NotFound() {
        // Arrange
        when(equipmentReturnService.processReturn(anyLong(), any(ProcessReturnRequestDTO.class)))
                .thenThrow(new NotFoundException("Return schedule not found."));

        // Act & Assert
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> equipmentReturnController.processReturn(1L, processReturnRequestDTO)
        );
        assertEquals("Return schedule not found.", exception.getMessage());
        verify(equipmentReturnService).processReturn(1L, processReturnRequestDTO);
    }

    @Test
    void getAllReturns_Success() {
        // Act
        ResponseEntity<List<EquipmentReturnResponseDTO>> response = equipmentReturnController.getAllReturns();

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTOList, response.getBody());
        verify(equipmentReturnService).getAll();
    }
}
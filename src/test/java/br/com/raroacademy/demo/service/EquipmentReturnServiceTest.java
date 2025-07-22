package br.com.raroacademy.demo.service;

import br.com.raroacademy.demo.domain.DTO.equipment.returns.CreateReturnScheduleRequestDTO;
import br.com.raroacademy.demo.domain.DTO.equipment.returns.EquipmentReturnResponseDTO;
import br.com.raroacademy.demo.domain.DTO.equipment.returns.MapperEquipmentReturn;
import br.com.raroacademy.demo.domain.DTO.equipment.returns.ProcessReturnRequestDTO;
import br.com.raroacademy.demo.domain.entities.EquipmentCollaboratorEntity;
import br.com.raroacademy.demo.domain.entities.EquipmentEntity;
import br.com.raroacademy.demo.domain.entities.EquipmentReturnEntity;
import br.com.raroacademy.demo.domain.enums.EquipmentStatus;
import br.com.raroacademy.demo.domain.enums.EquipmentType;
import br.com.raroacademy.demo.domain.enums.ReturnStatus;
import br.com.raroacademy.demo.exception.BusinessException;
import br.com.raroacademy.demo.exception.NotFoundException;
import br.com.raroacademy.demo.repository.EquipmentCollaboratorRepository;
import br.com.raroacademy.demo.repository.EquipmentReturnRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.context.MessageSource;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class EquipmentReturnServiceTest {

    @Mock
    private EquipmentReturnRepository equipmentReturnRepository;
    @Mock
    private EquipmentCollaboratorRepository equipmentCollaboratorRepository;
    @Mock
    private MapperEquipmentReturn mapperEquipmentReturn;
    @Mock
    private MessageSource messageSource;
    @Mock
    private EquipmentService equipmentService;
    @Mock
    private EquipmentCollaboratorService equipmentCollaboratorService;
    @Mock
    private StockService stockService;

    @InjectMocks
    private EquipmentReturnService equipmentReturnService;

    private EquipmentCollaboratorEntity equipmentCollaboratorEntity;
    private EquipmentReturnEntity pendingReturnEntity;
    private EquipmentReturnEntity returnedEntity;
    private CreateReturnScheduleRequestDTO createScheduleDTO;
    private ProcessReturnRequestDTO processReturnDTO;
    private EquipmentReturnResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        EquipmentEntity equipment = EquipmentEntity.builder().id(1L).type(EquipmentType.NOTEBOOK).status(EquipmentStatus.IN_USE).build();
        equipmentCollaboratorEntity = EquipmentCollaboratorEntity.builder().id(1L).equipment(equipment).build();
        pendingReturnEntity = EquipmentReturnEntity.builder().id(1L).equipmentCollaborator(equipmentCollaboratorEntity).status(ReturnStatus.PENDING).deliveryDate(LocalDate.now()).build();
        returnedEntity = EquipmentReturnEntity.builder().id(1L).equipmentCollaborator(equipmentCollaboratorEntity).status(ReturnStatus.RETURNED).build();

        createScheduleDTO = new CreateReturnScheduleRequestDTO(LocalDate.now().plusDays(1), 1L);
        processReturnDTO = new ProcessReturnRequestDTO(LocalDate.now(), EquipmentStatus.DEFECTIVE, "Devolvido com defeito");
        responseDTO = EquipmentReturnResponseDTO.builder().id(1L).status(ReturnStatus.PENDING).build();

        when(messageSource.getMessage(anyString(), any(), any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(equipmentCollaboratorRepository.findById(1L)).thenReturn(Optional.of(equipmentCollaboratorEntity));
        when(equipmentReturnRepository.findById(1L)).thenReturn(Optional.of(pendingReturnEntity));
        when(equipmentReturnRepository.save(any(EquipmentReturnEntity.class))).thenReturn(pendingReturnEntity);
        when(mapperEquipmentReturn.mapToResponseDTO(any(EquipmentReturnEntity.class))).thenReturn(responseDTO);
    }

    @Test
    void createSchedule_Success() {
        when(equipmentReturnRepository.existsByEquipmentCollaboratorIdAndStatus(1L, ReturnStatus.PENDING)).thenReturn(false);

        EquipmentReturnResponseDTO result = equipmentReturnService.createSchedule(createScheduleDTO);

        assertNotNull(result);
        assertEquals(ReturnStatus.PENDING, result.status());
        verify(equipmentReturnRepository).save(any(EquipmentReturnEntity.class));
    }

    @Test
    void createSchedule_EquipmentCollaboratorNotFound() {
        when(equipmentCollaboratorRepository.findById(1L)).thenReturn(Optional.empty());
        NotFoundException ex = assertThrows(NotFoundException.class, () -> equipmentReturnService.createSchedule(createScheduleDTO));
        assertEquals("equipment-collaborator.not-found", ex.getMessage());
    }

    @Test
    void createSchedule_PendingReturnAlreadyExists() {
        when(equipmentReturnRepository.existsByEquipmentCollaboratorIdAndStatus(1L, ReturnStatus.PENDING)).thenReturn(true);
        BusinessException ex = assertThrows(BusinessException.class, () -> equipmentReturnService.createSchedule(createScheduleDTO));
        assertEquals("equipment-return.pending.already-exists", ex.getMessage());
    }

    @Test
    void processReturn_Success_Defective() {
        EquipmentReturnResponseDTO result = equipmentReturnService.processReturn(1L, processReturnDTO);

        assertNotNull(result);
        verify(equipmentService).updateEquipmentStatus(1L, EquipmentStatus.DEFECTIVE);
        verify(stockService, never()).incrementStock(any());
        verify(equipmentCollaboratorService).finalizeLoan(1L);
        verify(equipmentReturnRepository).save(pendingReturnEntity);
        assertEquals(ReturnStatus.RETURNED, pendingReturnEntity.getStatus());
    }

    @Test
    void processReturn_Success_Available() {
        processReturnDTO = new ProcessReturnRequestDTO(LocalDate.now(), EquipmentStatus.AVAILABLE, "Tudo certo");

        EquipmentReturnResponseDTO result = equipmentReturnService.processReturn(1L, processReturnDTO);

        assertNotNull(result);
        verify(equipmentService).updateEquipmentStatus(1L, EquipmentStatus.AVAILABLE);
        verify(stockService).incrementStock(EquipmentType.NOTEBOOK);
        verify(equipmentCollaboratorService).finalizeLoan(1L);
        verify(equipmentReturnRepository).save(pendingReturnEntity);
        assertEquals(ReturnStatus.RETURNED, pendingReturnEntity.getStatus());
    }

    @Test
    void processReturn_ReturnNotFound() {
        when(equipmentReturnRepository.findById(1L)).thenReturn(Optional.empty());
        NotFoundException ex = assertThrows(NotFoundException.class, () -> equipmentReturnService.processReturn(1L, processReturnDTO));
        assertEquals("equipment-return.not-found", ex.getMessage());
    }

    @Test
    void processReturn_NotPending() {
        when(equipmentReturnRepository.findById(1L)).thenReturn(Optional.of(returnedEntity));
        BusinessException ex = assertThrows(BusinessException.class, () -> equipmentReturnService.processReturn(1L, processReturnDTO));
        assertEquals("equipment-return.not-pending", ex.getMessage());
    }

    @Test
    void getAll_Success() {
        when(equipmentReturnRepository.findAll()).thenReturn(Collections.singletonList(pendingReturnEntity));
        List<EquipmentReturnResponseDTO> result = equipmentReturnService.getAll();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }
}
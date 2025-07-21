package br.com.raroacademy.demo.service;

import br.com.raroacademy.demo.domain.DTO.collaborator.CollaboratorSummaryDTO;
import br.com.raroacademy.demo.domain.DTO.equipment.EquipmentSummaryDTO;
import br.com.raroacademy.demo.domain.DTO.equipment.collaborator.EquipmentCollaboratorResponseDTO;
import br.com.raroacademy.demo.domain.DTO.expected.returns.ExpectedReturnRequestDTO;
import br.com.raroacademy.demo.domain.DTO.expected.returns.ExpectedReturnResponseDTO;
import br.com.raroacademy.demo.domain.DTO.expected.returns.MapperExpectedReturn;
import br.com.raroacademy.demo.domain.entities.CollaboratorEntity;
import br.com.raroacademy.demo.domain.entities.EquipmentCollaboratorEntity;
import br.com.raroacademy.demo.domain.entities.EquipmentEntity;
import br.com.raroacademy.demo.domain.entities.ExpectedReturnEntity;
import br.com.raroacademy.demo.domain.enums.EquipmentStatus;
import br.com.raroacademy.demo.domain.enums.EquipmentType;
import br.com.raroacademy.demo.exception.DataIntegrityViolationException;
import br.com.raroacademy.demo.exception.NotFoundException;
import br.com.raroacademy.demo.repository.EquipmentCollaboratorRepository;
import br.com.raroacademy.demo.repository.ExpectedReturnRepository;
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
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ExpectedReturnServiceTest {

    @Mock
    private ExpectedReturnRepository expectedReturnRepository;

    @Mock
    private EquipmentCollaboratorRepository equipmentCollaboratorRepository;

    @Mock
    private MapperExpectedReturn mapperExpectedReturn;

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private ExpectedReturnService expectedReturnService;

    private ExpectedReturnRequestDTO requestDTO;
    private EquipmentCollaboratorEntity equipmentCollaboratorEntity;
    private ExpectedReturnEntity expectedReturnEntity;
    private ExpectedReturnResponseDTO responseDTO;
    private EquipmentCollaboratorResponseDTO equipmentCollaboratorResponseDTO;
    private LocalDate futureDate;

    @BeforeEach
    void setUp() {
        futureDate = LocalDate.now().plusMonths(1);

        CollaboratorEntity collaborator = CollaboratorEntity.builder()
                .id(1L)
                .name("Test Collaborator")
                .cpf("123.456.789-00")
                .email("collaborator@example.com")
                .phone("(11) 99999-9999")
                .addressId(1L)
                .contractStartDate(LocalDate.now().minusMonths(1))
                .build();

        EquipmentEntity equipment = EquipmentEntity.builder()
                .id(1L)
                .type(EquipmentType.NOTEBOOK)
                .serialNumber("SN12345")
                .brand("Test Brand")
                .model("Test Model")
                .specs("Test Specs")
                .acquisitionDate(LocalDate.now().minusMonths(6))
                .usageTimeMonths(6)
                .status(EquipmentStatus.IN_USE)
                .build();

        equipmentCollaboratorEntity = EquipmentCollaboratorEntity.builder()
                .id(1L)
                .collaborator(collaborator)
                .equipment(equipment)
                .deliveryDate(LocalDate.now().minusDays(7))
                .previsionDeliveryDate(LocalDate.now().minusDays(2))
                .deliveryStatus("Delivered")
                .notes("Test notes")
                .build();

        requestDTO = new ExpectedReturnRequestDTO(
                futureDate,
                1L
        );

        expectedReturnEntity = ExpectedReturnEntity.builder()
                .id(1L)
                .expectedReturnDate(futureDate)
                .equipmentCollaborator(equipmentCollaboratorEntity)
                .build();

        CollaboratorSummaryDTO collaboratorSummaryDTO = CollaboratorSummaryDTO.builder()
                .id(1L)
                .name("Test Collaborator")
                .email("collaborator@example.com")
                .build();

        EquipmentSummaryDTO equipmentSummaryDTO = EquipmentSummaryDTO.builder()
                .id(1L)
                .type("NOTEBOOK")
                .model("Test Model")
                .build();

        equipmentCollaboratorResponseDTO = EquipmentCollaboratorResponseDTO.builder()
                .id(1L)
                .deliveryDate(LocalDate.now().minusDays(7))
                .previsionDeliveryDate(LocalDate.now().minusDays(2))
                .deliveryStatus("Delivered")
                .notes("Test notes")
                .collaborator(collaboratorSummaryDTO)
                .equipment(equipmentSummaryDTO)
                .build();

        responseDTO = ExpectedReturnResponseDTO.builder()
                .id(1L)
                .expectedReturnDate(futureDate)
                .equipmentCollaborator(equipmentCollaboratorResponseDTO)
                .build();

        when(messageSource.getMessage(anyString(), any(), any(Locale.class)))
                .thenAnswer(invocation -> {
                    String code = invocation.getArgument(0);
                    return switch (code) {
                        case "equipment-collaborator.not-found" -> "Equipment-collaborator relationship not found";
                        case "expected-return.already-exists" -> "Expected return already exists for this equipment-collaborator";
                        case "expected-return.not-found" -> "Expected return not found";
                        default -> code;
                    };
                });

        // Setup mapper
        when(mapperExpectedReturn.toEntity(requestDTO, equipmentCollaboratorEntity)).thenReturn(expectedReturnEntity);
        when(mapperExpectedReturn.mapToResponseDTO(expectedReturnEntity)).thenReturn(responseDTO);
    }

    @Test
    void create_Success() {
        // Arrange
        when(equipmentCollaboratorRepository.findById(1L)).thenReturn(Optional.of(equipmentCollaboratorEntity));
        when(expectedReturnRepository.existsByEquipmentCollaboratorId(1L)).thenReturn(false);
        when(expectedReturnRepository.save(any(ExpectedReturnEntity.class))).thenReturn(expectedReturnEntity);

        // Act
        ExpectedReturnResponseDTO result = expectedReturnService.create(requestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(responseDTO, result);
        verify(equipmentCollaboratorRepository).findById(1L);
        verify(expectedReturnRepository).existsByEquipmentCollaboratorId(1L);
        verify(mapperExpectedReturn).toEntity(requestDTO, equipmentCollaboratorEntity);
        verify(expectedReturnRepository).save(expectedReturnEntity);
        verify(mapperExpectedReturn).mapToResponseDTO(expectedReturnEntity);
    }

    @Test
    void create_EquipmentCollaboratorNotFound() {
        // Arrange
        when(equipmentCollaboratorRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> expectedReturnService.create(requestDTO)
        );
        assertEquals("Equipment-collaborator relationship not found", exception.getMessage());
        verify(equipmentCollaboratorRepository).findById(1L);
        verify(expectedReturnRepository, never()).existsByEquipmentCollaboratorId(anyLong());
        verify(mapperExpectedReturn, never()).toEntity(any(), any());
        verify(expectedReturnRepository, never()).save(any());
    }

    @Test
    void create_AlreadyExists() {
        // Arrange
        when(equipmentCollaboratorRepository.findById(1L)).thenReturn(Optional.of(equipmentCollaboratorEntity));
        when(expectedReturnRepository.existsByEquipmentCollaboratorId(1L)).thenReturn(true);

        // Act & Assert
        DataIntegrityViolationException exception = assertThrows(
                DataIntegrityViolationException.class,
                () -> expectedReturnService.create(requestDTO)
        );
        assertEquals("Expected return already exists for this equipment-collaborator", exception.getMessage());
        verify(equipmentCollaboratorRepository).findById(1L);
        verify(expectedReturnRepository).existsByEquipmentCollaboratorId(1L);
        verify(mapperExpectedReturn, never()).toEntity(any(), any());
        verify(expectedReturnRepository, never()).save(any());
    }

    @Test
    void getById_Success() {
        // Arrange
        when(expectedReturnRepository.findById(1L)).thenReturn(Optional.of(expectedReturnEntity));

        // Act
        ExpectedReturnResponseDTO result = expectedReturnService.getById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(responseDTO, result);
        verify(expectedReturnRepository).findById(1L);
        verify(mapperExpectedReturn).mapToResponseDTO(expectedReturnEntity);
    }

    @Test
    void getById_NotFound() {
        // Arrange
        when(expectedReturnRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> expectedReturnService.getById(1L)
        );
        assertEquals("Expected return not found", exception.getMessage());
        verify(expectedReturnRepository).findById(1L);
        verify(mapperExpectedReturn, never()).mapToResponseDTO(any());
    }

    @Test
    void getAll_Success() {
        // Arrange
        List<ExpectedReturnEntity> entities = Arrays.asList(expectedReturnEntity);
        when(expectedReturnRepository.findAll()).thenReturn(entities);
        when(mapperExpectedReturn.mapToResponseDTO(expectedReturnEntity)).thenReturn(responseDTO);

        // Act
        List<ExpectedReturnResponseDTO> results = expectedReturnService.getAll();

        // Assert
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals(responseDTO, results.get(0));
        verify(expectedReturnRepository).findAll();
        verify(mapperExpectedReturn).mapToResponseDTO(expectedReturnEntity);
    }

    @Test
    void update_Success() {
        // Arrange
        when(expectedReturnRepository.findById(1L)).thenReturn(Optional.of(expectedReturnEntity));
        when(equipmentCollaboratorRepository.findById(1L)).thenReturn(Optional.of(equipmentCollaboratorEntity));
        when(expectedReturnRepository.save(expectedReturnEntity)).thenReturn(expectedReturnEntity);

        // Act
        ExpectedReturnResponseDTO result = expectedReturnService.update(1L, requestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(responseDTO, result);
        verify(expectedReturnRepository).findById(1L);
        verify(equipmentCollaboratorRepository).findById(1L);
        verify(expectedReturnRepository).save(expectedReturnEntity);
        verify(mapperExpectedReturn).mapToResponseDTO(expectedReturnEntity);
        assertEquals(futureDate, expectedReturnEntity.getExpectedReturnDate());
        assertEquals(equipmentCollaboratorEntity, expectedReturnEntity.getEquipmentCollaborator());
    }

    @Test
    void update_NotFound() {
        // Arrange
        when(expectedReturnRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> expectedReturnService.update(1L, requestDTO)
        );
        assertEquals("Expected return not found", exception.getMessage());
        verify(expectedReturnRepository).findById(1L);
        verify(equipmentCollaboratorRepository, never()).findById(anyLong());
        verify(expectedReturnRepository, never()).save(any());
    }

    @Test
    void update_EquipmentCollaboratorNotFound() {
        // Arrange
        when(expectedReturnRepository.findById(1L)).thenReturn(Optional.of(expectedReturnEntity));
        when(equipmentCollaboratorRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> expectedReturnService.update(1L, requestDTO)
        );
        assertEquals("Equipment-collaborator relationship not found", exception.getMessage());
        verify(expectedReturnRepository).findById(1L);
        verify(equipmentCollaboratorRepository).findById(1L);
        verify(expectedReturnRepository, never()).save(any());
    }

    @Test
    void update_AlreadyExists() {
        // Arrange
        ExpectedReturnRequestDTO newRequestDTO = new ExpectedReturnRequestDTO(
                futureDate,
                2L
        );

        EquipmentCollaboratorEntity newEquipmentCollaborator = EquipmentCollaboratorEntity.builder()
                .id(2L)
                .build();

        when(expectedReturnRepository.findById(1L)).thenReturn(Optional.of(expectedReturnEntity));
        when(equipmentCollaboratorRepository.findById(2L)).thenReturn(Optional.of(newEquipmentCollaborator));
        when(expectedReturnRepository.existsByEquipmentCollaboratorId(2L)).thenReturn(true);

        // Act & Assert
        DataIntegrityViolationException exception = assertThrows(
                DataIntegrityViolationException.class,
                () -> expectedReturnService.update(1L, newRequestDTO)
        );
        assertEquals("Expected return already exists for this equipment-collaborator", exception.getMessage());
        verify(expectedReturnRepository).findById(1L);
        verify(equipmentCollaboratorRepository).findById(2L);
        verify(expectedReturnRepository).existsByEquipmentCollaboratorId(2L);
        verify(expectedReturnRepository, never()).save(any());
    }

    @Test
    void delete_Success() {
        // Arrange
        when(expectedReturnRepository.existsById(1L)).thenReturn(true);

        // Act
        expectedReturnService.delete(1L);

        // Assert
        verify(expectedReturnRepository).existsById(1L);
        verify(expectedReturnRepository).deleteById(1L);
    }

    @Test
    void delete_NotFound() {
        // Arrange
        when(expectedReturnRepository.existsById(1L)).thenReturn(false);

        // Act & Assert
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> expectedReturnService.delete(1L)
        );
        assertEquals("Expected return not found", exception.getMessage());
        verify(expectedReturnRepository).existsById(1L);
        verify(expectedReturnRepository, never()).deleteById(anyLong());
    }
}
package br.com.raroacademy.demo.service;

import br.com.raroacademy.demo.domain.DTO.collaborator.CollaboratorSummaryDTO;
import br.com.raroacademy.demo.domain.DTO.collaborator.MapperCollaborator;
import br.com.raroacademy.demo.domain.DTO.equipment.EquipmentSummaryDTO;
import br.com.raroacademy.demo.domain.DTO.equipment.MapperEquipment;
import br.com.raroacademy.demo.domain.DTO.equipment.collaborator.EquipmentCollaboratorRequestDTO;
import br.com.raroacademy.demo.domain.DTO.equipment.collaborator.EquipmentCollaboratorResponseDTO;
import br.com.raroacademy.demo.domain.DTO.equipment.collaborator.MapperEquipmentCollaborator;
import br.com.raroacademy.demo.domain.entities.AddressEntity;
import br.com.raroacademy.demo.domain.entities.CollaboratorEntity;
import br.com.raroacademy.demo.domain.entities.EquipmentCollaboratorEntity;
import br.com.raroacademy.demo.domain.entities.EquipmentEntity;
import br.com.raroacademy.demo.domain.enums.EquipmentStatus;
import br.com.raroacademy.demo.domain.enums.EquipmentType;
import br.com.raroacademy.demo.exception.DataIntegrityViolationException;
import br.com.raroacademy.demo.exception.NotFoundException;
import br.com.raroacademy.demo.repository.AddressRepository;
import br.com.raroacademy.demo.repository.CollaboratorRepository;
import br.com.raroacademy.demo.repository.EquipmentCollaboratorRepository;
import br.com.raroacademy.demo.repository.EquipmentRepository;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class EquipmentCollaboratorServiceTest {

    @Mock
    private EquipmentCollaboratorRepository equipmentCollaboratorRepository;

    @Mock
    private CollaboratorRepository collaboratorRepository;

    @Mock
    private EquipmentRepository equipmentRepository;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private StockService stockService;

    @Mock
    private MapperEquipmentCollaborator mapperEquipmentCollaborator;

    @Mock
    private MapperCollaborator mapperCollaborator;

    @Mock
    private MapperEquipment mapperEquipment;

    @Mock
    private DeliveryTimeService deliveryTimeService;

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private EquipmentCollaboratorService equipmentCollaboratorService;

    private CollaboratorEntity collaborator;
    private EquipmentEntity availableEquipment;
    private EquipmentEntity inUseEquipment;
    private AddressEntity address;
    private EquipmentCollaboratorEntity equipmentCollaboratorEntity;
    private EquipmentCollaboratorRequestDTO requestDTO;
    private EquipmentCollaboratorResponseDTO responseDTO;
    private CollaboratorSummaryDTO collaboratorSummaryDTO;
    private EquipmentSummaryDTO equipmentSummaryDTO;
    private LocalDate today;
    private LocalDate tomorrow;
    private LocalDate yesterday;

    @BeforeEach
    void setUp() {
        today = LocalDate.now();
        tomorrow = today.plusDays(1);
        yesterday = today.minusDays(1);

        collaborator = CollaboratorEntity.builder()
                .id(1L)
                .name("Test Collaborator")
                .cpf("123.456.789-00")
                .email("collaborator@example.com")
                .phone("(11) 99999-9999")
                .addressId(1L)
                .contractStartDate(today.minusMonths(1))
                .build();

        availableEquipment = EquipmentEntity.builder()
                .id(1L)
                .type(EquipmentType.NOTEBOOK)
                .serialNumber("SN12345")
                .brand("Test Brand")
                .model("Test Model")
                .specs("Test Specs")
                .acquisitionDate(today.minusMonths(6))
                .usageTimeMonths(6)
                .status(EquipmentStatus.AVAILABLE)
                .build();

        inUseEquipment = EquipmentEntity.builder()
                .id(2L)
                .type(EquipmentType.NOTEBOOK)
                .serialNumber("SN67890")
                .brand("Test Brand")
                .model("Test Model")
                .specs("Test Specs")
                .acquisitionDate(today.minusMonths(6))
                .usageTimeMonths(6)
                .status(EquipmentStatus.IN_USE)
                .build();

        address = AddressEntity.builder()
                .id(1L)
                .street("Test Street")
                .number("123")
                .neighborhood("Test Neighborhood")
                .city("Test City")
                .state("SP")
                .cep("12345-678")
                .country("Brazil")
                .build();

        requestDTO = new EquipmentCollaboratorRequestDTO(
                1L,
                1L,
                today,
                null,
                "Pending",
                "Test notes"
        );

        collaboratorSummaryDTO = CollaboratorSummaryDTO.builder()
                .id(1L)
                .name("Test Collaborator")
                .email("collaborator@example.com")
                .build();

        equipmentSummaryDTO = EquipmentSummaryDTO.builder()
                .id(1L)
                .type("NOTEBOOK")
                .model("Test Model")
                .build();

        equipmentCollaboratorEntity = EquipmentCollaboratorEntity.builder()
                .id(1L)
                .collaborator(collaborator)
                .equipment(availableEquipment)
                .deliveryDate(today)
                .previsionDeliveryDate(tomorrow)
                .deliveryStatus("Pending")
                .notes("Test notes")
                .build();

        responseDTO = EquipmentCollaboratorResponseDTO.builder()
                .id(1L)
                .deliveryDate(today)
                .previsionDeliveryDate(tomorrow)
                .deliveryStatus("Pending")
                .notes("Test notes")
                .collaborator(collaboratorSummaryDTO)
                .equipment(equipmentSummaryDTO)
                .build();

        when(messageSource.getMessage(anyString(), any(), any(Locale.class)))
                .thenAnswer(invocation -> {
                    String code = invocation.getArgument(0);
                    return switch (code) {
                        case "collaborator.not-found" -> "Collaborator not found";
                        case "equipment.not-found" -> "Equipment not found";
                        case "equipment.unavailable.status" -> "Equipment is not available";
                        case "address.not-found" -> "Address not found";
                        case "equipment-collaborator.not-found" -> "Equipment-collaborator relationship not found";
                        case "return.date.future" -> "Return date cannot be in the future";
                        default -> code;
                    };
                });

        when(mapperEquipmentCollaborator.toEntity(any(EquipmentCollaboratorRequestDTO.class), any(CollaboratorEntity.class), any(EquipmentEntity.class))).thenReturn(equipmentCollaboratorEntity);
        when(mapperEquipmentCollaborator.toResponse(any(EquipmentCollaboratorEntity.class), any(CollaboratorSummaryDTO.class), any(EquipmentSummaryDTO.class))).thenReturn(responseDTO);
        when(mapperEquipmentCollaborator.mapToResponseDTO(any(EquipmentCollaboratorEntity.class))).thenReturn(responseDTO);
        when(mapperCollaborator.toSummaryResponse(any(CollaboratorEntity.class))).thenReturn(collaboratorSummaryDTO);
        when(mapperEquipment.toSummaryResponse(any(EquipmentEntity.class))).thenReturn(equipmentSummaryDTO);
        when(deliveryTimeService.calculate(anyString(), any(LocalDate.class))).thenReturn(tomorrow);
    }

    @Test
    void create_Success() {
        // Arrange
        when(collaboratorRepository.findById(1L)).thenReturn(Optional.of(collaborator));
        when(equipmentRepository.findById(1L)).thenReturn(Optional.of(availableEquipment));
        when(addressRepository.findById(1L)).thenReturn(Optional.of(address));
        when(equipmentCollaboratorRepository.save(any(EquipmentCollaboratorEntity.class))).thenReturn(equipmentCollaboratorEntity);

        // Act
        EquipmentCollaboratorResponseDTO result = equipmentCollaboratorService.create(requestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(responseDTO, result);
        verify(collaboratorRepository).findById(1L);
        verify(equipmentRepository).findById(1L);
        verify(addressRepository).findById(1L);
        verify(stockService).decrementStock(availableEquipment.getType());
        verify(equipmentRepository).save(availableEquipment);
        verify(equipmentCollaboratorRepository).save(any(EquipmentCollaboratorEntity.class));
        assertEquals(EquipmentStatus.IN_USE, availableEquipment.getStatus());
    }

    @Test
    void create_CollaboratorNotFound() {
        // Arrange
        when(collaboratorRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> equipmentCollaboratorService.create(requestDTO)
        );
        assertEquals("Collaborator not found", exception.getMessage());
        verify(collaboratorRepository).findById(1L);
        verify(equipmentRepository, never()).findById(anyLong());
        verify(stockService, never()).decrementStock(any());
    }

    @Test
    void create_EquipmentNotFound() {
        // Arrange
        when(collaboratorRepository.findById(1L)).thenReturn(Optional.of(collaborator));
        when(equipmentRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> equipmentCollaboratorService.create(requestDTO)
        );
        assertEquals("Equipment not found", exception.getMessage());
        verify(collaboratorRepository).findById(1L);
        verify(equipmentRepository).findById(1L);
        verify(stockService, never()).decrementStock(any());
    }

    @Test
    void create_EquipmentNotAvailable() {
        // Arrange
        when(collaboratorRepository.findById(1L)).thenReturn(Optional.of(collaborator));
        when(equipmentRepository.findById(1L)).thenReturn(Optional.of(inUseEquipment));

        // Act & Assert
        DataIntegrityViolationException exception = assertThrows(
                DataIntegrityViolationException.class,
                () -> equipmentCollaboratorService.create(requestDTO)
        );
        assertEquals("Equipment is not available", exception.getMessage());
        verify(collaboratorRepository).findById(1L);
        verify(equipmentRepository).findById(1L);
        verify(stockService, never()).decrementStock(any());
    }

    @Test
    void create_AddressNotFound() {
        // Arrange
        when(collaboratorRepository.findById(1L)).thenReturn(Optional.of(collaborator));
        when(equipmentRepository.findById(1L)).thenReturn(Optional.of(availableEquipment));
        when(addressRepository.findById(1L)).thenReturn(Optional.empty());
        doAnswer(invocation -> {
            availableEquipment.setStatus(EquipmentStatus.IN_USE);
            return null;
        }).when(equipmentRepository).save(availableEquipment);


        // Act & Assert
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> equipmentCollaboratorService.create(requestDTO)
        );
        assertEquals("Address not found", exception.getMessage());
        verify(collaboratorRepository).findById(1L);
        verify(equipmentRepository).findById(1L);
        verify(stockService).decrementStock(availableEquipment.getType());
        verify(equipmentRepository).save(availableEquipment);
        verify(addressRepository).findById(1L);
    }

    @Test
    void getById_Success() {
        // Arrange
        when(equipmentCollaboratorRepository.findById(1L)).thenReturn(Optional.of(equipmentCollaboratorEntity));

        // Act
        EquipmentCollaboratorResponseDTO result = equipmentCollaboratorService.getById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(responseDTO, result);
        verify(equipmentCollaboratorRepository).findById(1L);
    }

    @Test
    void getById_NotFound() {
        // Arrange
        when(equipmentCollaboratorRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> equipmentCollaboratorService.getById(1L)
        );
        assertEquals("Equipment-collaborator relationship not found", exception.getMessage());
        verify(equipmentCollaboratorRepository).findById(1L);
    }

    @Test
    void getAll_Success() {
        // Arrange
        List<EquipmentCollaboratorEntity> entities = Arrays.asList(equipmentCollaboratorEntity);
        when(equipmentCollaboratorRepository.findAll()).thenReturn(entities);

        // Act
        List<EquipmentCollaboratorResponseDTO> results = equipmentCollaboratorService.getAll();

        // Assert
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals(responseDTO, results.get(0));
        verify(equipmentCollaboratorRepository).findAll();
    }

    @Test
    void update_Success_WithoutReturn() {
        // Arrange
        when(equipmentCollaboratorRepository.findById(1L)).thenReturn(Optional.of(equipmentCollaboratorEntity));
        when(collaboratorRepository.findById(1L)).thenReturn(Optional.of(collaborator));
        when(equipmentRepository.findById(1L)).thenReturn(Optional.of(availableEquipment));
        when(equipmentCollaboratorRepository.save(any(EquipmentCollaboratorEntity.class))).thenReturn(equipmentCollaboratorEntity);

        // Act
        EquipmentCollaboratorResponseDTO result = equipmentCollaboratorService.update(1L, requestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(responseDTO, result);
        verify(equipmentCollaboratorRepository).findById(1L);
        verify(collaboratorRepository).findById(1L);
        verify(equipmentRepository).findById(1L);
        verify(equipmentCollaboratorRepository).save(any(EquipmentCollaboratorEntity.class));
        verify(stockService, never()).incrementStock(any());
    }

    @Test
    void update_Success_WithReturn() {
        // Arrange
        EquipmentCollaboratorRequestDTO returnRequest = new EquipmentCollaboratorRequestDTO(
                requestDTO.collaboratorId(),
                requestDTO.equipmentId(),
                requestDTO.deliveryDate(),
                yesterday,
                "Returned",
                "Returned notes"
        );
        when(equipmentCollaboratorRepository.findById(1L)).thenReturn(Optional.of(equipmentCollaboratorEntity));
        when(collaboratorRepository.findById(1L)).thenReturn(Optional.of(collaborator));
        when(equipmentRepository.findById(1L)).thenReturn(Optional.of(availableEquipment));
        when(equipmentCollaboratorRepository.save(any(EquipmentCollaboratorEntity.class))).thenReturn(equipmentCollaboratorEntity);
        doAnswer(invocation -> {
            EquipmentEntity savedEquipment = invocation.getArgument(0);
            savedEquipment.setStatus(EquipmentStatus.AVAILABLE);
            return null;
        }).when(equipmentRepository).save(any(EquipmentEntity.class));

        // Act
        equipmentCollaboratorService.update(1L, returnRequest);

        // Assert
        verify(equipmentCollaboratorRepository).findById(1L);
        verify(collaboratorRepository).findById(1L);
        verify(stockService).incrementStock(availableEquipment.getType());
        verify(equipmentRepository).save(availableEquipment);
        assertEquals(EquipmentStatus.AVAILABLE, availableEquipment.getStatus());
    }

    @Test
    void update_NotFound() {
        // Arrange
        when(equipmentCollaboratorRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> equipmentCollaboratorService.update(1L, requestDTO)
        );
        assertEquals("Equipment-collaborator relationship not found", exception.getMessage());
        verify(equipmentCollaboratorRepository).findById(1L);
        verify(collaboratorRepository, never()).findById(anyLong());
    }

    @Test
    void update_FutureReturnDate() {
        // Arrange
        EquipmentCollaboratorRequestDTO futureReturnRequest = new EquipmentCollaboratorRequestDTO(
                requestDTO.collaboratorId(),
                requestDTO.equipmentId(),
                requestDTO.deliveryDate(),
                tomorrow,
                "Pending Return",
                requestDTO.notes()
        );
        when(equipmentCollaboratorRepository.findById(1L)).thenReturn(Optional.of(equipmentCollaboratorEntity));

        // Act & Assert
        DataIntegrityViolationException exception = assertThrows(
                DataIntegrityViolationException.class,
                () -> equipmentCollaboratorService.update(1L, futureReturnRequest)
        );
        assertEquals("Return date cannot be in the future", exception.getMessage());
        verify(equipmentCollaboratorRepository).findById(1L);
        verify(stockService, never()).incrementStock(any());
    }

    @Test
    void delete_Success() {
        // Arrange
        when(equipmentCollaboratorRepository.existsById(1L)).thenReturn(true);

        // Act
        equipmentCollaboratorService.delete(1L);

        // Assert
        verify(equipmentCollaboratorRepository).existsById(1L);
        verify(equipmentCollaboratorRepository).deleteById(1L);
    }

    @Test
    void delete_NotFound() {
        // Arrange
        when(equipmentCollaboratorRepository.existsById(1L)).thenReturn(false);

        // Act & Assert
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> equipmentCollaboratorService.delete(1L)
        );
        assertEquals("Equipment-collaborator relationship not found", exception.getMessage());
        verify(equipmentCollaboratorRepository).existsById(1L);
        verify(equipmentCollaboratorRepository, never()).deleteById(anyLong());
    }
}
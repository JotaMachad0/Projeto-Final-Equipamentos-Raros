package br.com.raroacademy.demo.service;

import br.com.raroacademy.demo.commons.i18n.I18nUtil;
import br.com.raroacademy.demo.domain.DTO.equipment.EquipmentRequestDTO;
import br.com.raroacademy.demo.domain.DTO.equipment.EquipmentResponseDTO;
import br.com.raroacademy.demo.domain.DTO.equipment.MapperEquipment;
import br.com.raroacademy.demo.domain.entities.EquipmentEntity;
import br.com.raroacademy.demo.domain.enums.EquipmentStatus;
import br.com.raroacademy.demo.domain.enums.EquipmentType;
import br.com.raroacademy.demo.exception.NotFoundException;
import br.com.raroacademy.demo.repository.EquipmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class EquipmentServiceTest {

    @Mock
    private EquipmentRepository equipmentRepository;

    @Mock
    private MapperEquipment mapperEquipment;

    @Mock
    private StockService stockService;

    @Mock
    private I18nUtil i18n;

    @InjectMocks
    private EquipmentService equipmentService;

    private EquipmentEntity equipmentEntity;
    private EquipmentRequestDTO equipmentRequestDTO;
    private EquipmentResponseDTO equipmentResponseDTO;
    private LocalDate acquisitionDate;

    @BeforeEach
    void setUp() {
        acquisitionDate = LocalDate.of(2024, 1, 15);

        equipmentEntity = EquipmentEntity.builder()
                .id(1L)
                .type(EquipmentType.NOTEBOOK)
                .serialNumber("SN-123456789")
                .brand("Dell")
                .model("Inspiron 15 3000")
                .specs("8GB RAM, SSD 256GB")
                .acquisitionDate(acquisitionDate)
                .usageTimeMonths(12)
                .status(EquipmentStatus.AVAILABLE)
                .build();

        equipmentRequestDTO = new EquipmentRequestDTO(
                "NOTEBOOK",
                "SN-123456789",
                "Dell",
                "Inspiron 15 3000",
                "8GB RAM, SSD 256GB",
                acquisitionDate,
                12,
                EquipmentStatus.AVAILABLE
        );

        equipmentResponseDTO = EquipmentResponseDTO.builder()
                .id(1L)
                .type("NOTEBOOK")
                .serialNumber("SN-123456789")
                .brand("Dell")
                .model("Inspiron 15 3000")
                .specs("8GB RAM, SSD 256GB")
                .acquisitionDate(acquisitionDate)
                .usageTimeMonths(12)
                .status(EquipmentStatus.AVAILABLE)
                .build();

        when(i18n.getMessage("equipment.not.found")).thenReturn("Equipment not found");
        when(i18n.getMessage("equipment.serialNumber.already.exists")).thenReturn("Serial number already exists");
    }

    @Test
    void create_Success() {
        // Arrange
        when(mapperEquipment.toEntity(equipmentRequestDTO)).thenReturn(equipmentEntity);
        when(equipmentRepository.save(equipmentEntity)).thenReturn(equipmentEntity);
        when(mapperEquipment.toDTO(equipmentEntity)).thenReturn(equipmentResponseDTO);
        doNothing().when(stockService).incrementStock(any(EquipmentType.class));

        // Act
        EquipmentResponseDTO result = equipmentService.create(equipmentRequestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(equipmentResponseDTO, result);
        verify(mapperEquipment).toEntity(equipmentRequestDTO);
        verify(equipmentRepository).save(equipmentEntity);
        verify(stockService).incrementStock(equipmentEntity.getType());
        verify(mapperEquipment).toDTO(equipmentEntity);
    }

    @Test
    void create_DataIntegrityViolation() {
        // Arrange
        when(mapperEquipment.toEntity(equipmentRequestDTO)).thenReturn(equipmentEntity);
        when(equipmentRepository.save(equipmentEntity)).thenThrow(new DataIntegrityViolationException("Duplicate entry"));

        // Act & Assert
        assertThrows(DataIntegrityViolationException.class, () -> equipmentService.create(equipmentRequestDTO));
        verify(mapperEquipment).toEntity(equipmentRequestDTO);
        verify(equipmentRepository).save(equipmentEntity);
        verify(stockService, never()).incrementStock(any(EquipmentType.class));
    }

    @Test
    void getEquipment_Success() {
        // Arrange
        when(equipmentRepository.findById(1L)).thenReturn(Optional.of(equipmentEntity));
        when(mapperEquipment.toDTO(equipmentEntity)).thenReturn(equipmentResponseDTO);

        // Act
        EquipmentResponseDTO result = equipmentService.getEquipment(1L);

        // Assert
        assertNotNull(result);
        assertEquals(equipmentResponseDTO, result);
        verify(equipmentRepository).findById(1L);
        verify(mapperEquipment).toDTO(equipmentEntity);
    }

    @Test
    void getEquipment_NotFound() {
        // Arrange
        when(equipmentRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> equipmentService.getEquipment(1L)
        );
        assertEquals("Equipment not found", exception.getMessage());
        verify(equipmentRepository).findById(1L);
        verify(mapperEquipment, never()).toDTO(any());
    }

    @Test
    void update_Success() {
        // Arrange
        when(equipmentRepository.findById(1L)).thenReturn(Optional.of(equipmentEntity));
        when(equipmentRepository.existsBySerialNumber(equipmentRequestDTO.serialNumber())).thenReturn(false);
        when(mapperEquipment.toUpdateEquipment(equipmentEntity, equipmentRequestDTO)).thenReturn(equipmentEntity);
        when(equipmentRepository.save(equipmentEntity)).thenReturn(equipmentEntity);
        when(mapperEquipment.toDTO(equipmentEntity)).thenReturn(equipmentResponseDTO);

        // Act
        EquipmentResponseDTO result = equipmentService.update(1L, equipmentRequestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(equipmentResponseDTO, result);
        verify(equipmentRepository).findById(1L);
        verify(mapperEquipment).toUpdateEquipment(equipmentEntity, equipmentRequestDTO);
        verify(equipmentRepository).save(equipmentEntity);
        verify(mapperEquipment).toDTO(equipmentEntity);
    }

    @Test
    void update_NotFound() {
        // Arrange
        when(equipmentRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> equipmentService.update(1L, equipmentRequestDTO)
        );
        assertEquals("Equipment not found", exception.getMessage());
        verify(equipmentRepository).findById(1L);
        verify(mapperEquipment, never()).toUpdateEquipment(any(), any());
        verify(equipmentRepository, never()).save(any());
    }

    @Test
    void update_SerialNumberAlreadyExists() {
        // Arrange
        EquipmentRequestDTO newSerialRequest = new EquipmentRequestDTO(
                "NOTEBOOK",
                "SN-987654321",
                "Dell",
                "Inspiron 15 3000",
                "8GB RAM, SSD 256GB",
                acquisitionDate,
                12,
                EquipmentStatus.AVAILABLE
        );

        when(equipmentRepository.findById(1L)).thenReturn(Optional.of(equipmentEntity));
        when(equipmentRepository.existsBySerialNumber(newSerialRequest.serialNumber())).thenReturn(true);

        // Act & Assert
        DataIntegrityViolationException exception = assertThrows(
                DataIntegrityViolationException.class,
                () -> equipmentService.update(1L, newSerialRequest)
        );
        assertEquals("Serial number already exists", exception.getMessage());
        verify(equipmentRepository).findById(1L);
        verify(equipmentRepository).existsBySerialNumber(newSerialRequest.serialNumber());
        verify(mapperEquipment, never()).toUpdateEquipment(any(), any());
        verify(equipmentRepository, never()).save(any());
    }

    @Test
    void delete_Success() {
        // Arrange
        when(equipmentRepository.findById(1L)).thenReturn(Optional.of(equipmentEntity));
        doNothing().when(equipmentRepository).delete(equipmentEntity);
        doNothing().when(stockService).decrementStock(any(EquipmentType.class));

        // Act
        equipmentService.delete(1L);

        // Assert
        verify(equipmentRepository).findById(1L);
        verify(equipmentRepository).delete(equipmentEntity);
        verify(stockService).decrementStock(equipmentEntity.getType());
    }

    @Test
    void delete_NotFound() {
        // Arrange
        when(equipmentRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> equipmentService.delete(1L)
        );
        assertEquals("Equipment not found", exception.getMessage());
        verify(equipmentRepository).findById(1L);
        verify(equipmentRepository, never()).delete(any());
        verify(stockService, never()).decrementStock(any(EquipmentType.class));
    }

    @Test
    void getAll_Success() {
        // Arrange
        List<EquipmentEntity> equipmentEntities = Arrays.asList(equipmentEntity);
        List<EquipmentResponseDTO> equipmentResponseDTOs = Arrays.asList(equipmentResponseDTO);

        when(equipmentRepository.findAll()).thenReturn(equipmentEntities);
        when(mapperEquipment.toDTO(equipmentEntity)).thenReturn(equipmentResponseDTO);

        // Act
        List<EquipmentResponseDTO> result = equipmentService.getAll();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(equipmentResponseDTO, result.get(0));
        verify(equipmentRepository).findAll();
        verify(mapperEquipment).toDTO(equipmentEntity);
    }

    @Test
    void getAll_EmptyList() {
        // Arrange
        when(equipmentRepository.findAll()).thenReturn(List.of());

        // Act
        List<EquipmentResponseDTO> result = equipmentService.getAll();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(equipmentRepository).findAll();
        verify(mapperEquipment, never()).toDTO(any());
    }
}
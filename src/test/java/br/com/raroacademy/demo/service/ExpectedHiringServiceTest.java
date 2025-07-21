package br.com.raroacademy.demo.service;

import br.com.raroacademy.demo.commons.i18n.I18nUtil;
import br.com.raroacademy.demo.domain.DTO.expected.hirings.ExpectedHiringRequestDTO;
import br.com.raroacademy.demo.domain.DTO.expected.hirings.ExpectedHiringResponseDTO;
import br.com.raroacademy.demo.domain.DTO.expected.hirings.MapperExpectedHiring;
import br.com.raroacademy.demo.domain.entities.ExpectedHiringEntity;
import br.com.raroacademy.demo.domain.enums.ExpectedHiringStatus;
import br.com.raroacademy.demo.domain.enums.Region;
import br.com.raroacademy.demo.exception.ExpectedHiringAlreadyExistsException;
import br.com.raroacademy.demo.exception.InvalidStatusException;
import br.com.raroacademy.demo.exception.NotFoundException;
import br.com.raroacademy.demo.repository.ExpectedHiringRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ExpectedHiringServiceTest {

    @Mock
    private ExpectedHiringRepository expectedHiringRepository;

    @Mock
    private MapperExpectedHiring mapperExpectedHiring;

    @Mock
    private I18nUtil i18nUtil;

    @InjectMocks
    private ExpectedHiringService expectedHiringService;

    private ExpectedHiringRequestDTO requestDTO;
    private ExpectedHiringEntity entity;
    private ExpectedHiringEntity expiredEntity;
    private ExpectedHiringEntity processedEntity;
    private ExpectedHiringResponseDTO responseDTO;
    private LocalDate futureDate;
    private LocalDate pastDate;

    @BeforeEach
    void setUp() {
        futureDate = LocalDate.now().plusMonths(1);
        pastDate = LocalDate.now().minusDays(1);

        requestDTO = new ExpectedHiringRequestDTO(
                futureDate,
                "Developer",
                "Notebook, Mouse, Keyboard",
                Region.SUDESTE
        );

        entity = ExpectedHiringEntity.builder()
                .id(1L)
                .expectedHireDate(futureDate)
                .position("Developer")
                .equipmentRequirements("Notebook, Mouse, Keyboard")
                .region(Region.SUDESTE)
                .expectedHiringStatus(ExpectedHiringStatus.CREATED)
                .build();

        expiredEntity = ExpectedHiringEntity.builder()
                .id(2L)
                .expectedHireDate(pastDate)
                .position("Designer")
                .equipmentRequirements("Notebook, Graphics Tablet")
                .region(Region.NORDESTE)
                .expectedHiringStatus(ExpectedHiringStatus.CREATED)
                .build();

        processedEntity = ExpectedHiringEntity.builder()
                .id(3L)
                .expectedHireDate(pastDate)
                .position("Manager")
                .equipmentRequirements("Notebook, Phone")
                .region(Region.CENTRO_OESTE)
                .expectedHiringStatus(ExpectedHiringStatus.PROCESSED)
                .build();

        responseDTO = ExpectedHiringResponseDTO.builder()
                .id(1L)
                .expectedHireDate(futureDate)
                .position("Developer")
                .equipmentRequirements("Notebook, Mouse, Keyboard")
                .region(Region.SUDESTE)
                .expectedHiringStatus(ExpectedHiringStatus.CREATED)
                .build();

        when(i18nUtil.getMessage("expected.hiring.not.found")).thenReturn("Expected hiring not found");
        when(i18nUtil.getMessage("expected.hiring.already.exists", 1L)).thenReturn("Expected hiring already exists with ID 1");
        when(i18nUtil.getMessage("expected.hiring.already.exists", 1L, "CREATED", "Notebook, Mouse, Keyboard"))
                .thenReturn("Expected hiring already exists with ID 1, status CREATED, requirements: Notebook, Mouse, Keyboard");
        when(i18nUtil.getMessage("expected.hiring.unchanged", 1L)).thenReturn("Expected hiring with ID 1 unchanged");
        when(i18nUtil.getMessage("expected.hiring.invalid.status")).thenReturn("Invalid status for expected hiring");
        when(i18nUtil.getMessage("expected.hiring.status.CREATED")).thenReturn("CREATED");

        when(mapperExpectedHiring.toExpectedHiring(requestDTO)).thenReturn(entity);
        when(mapperExpectedHiring.toExpectedHiringResponseDTO(entity)).thenReturn(responseDTO);
        when(mapperExpectedHiring.toApplyUpdates(entity, requestDTO)).thenReturn(entity);
        when(mapperExpectedHiring.toExpectedHiringList(anyList())).thenAnswer(invocation -> {
            List<ExpectedHiringEntity> entities = invocation.getArgument(0);
            return entities.stream()
                    .map(e -> ExpectedHiringResponseDTO.builder()
                            .id(e.getId())
                            .expectedHireDate(e.getExpectedHireDate())
                            .position(e.getPosition())
                            .equipmentRequirements(e.getEquipmentRequirements())
                            .region(e.getRegion())
                            .expectedHiringStatus(e.getExpectedHiringStatus())
                            .build())
                    .toList();
        });
    }

    @Test
    void create_Success() {
        // Arrange
        when(expectedHiringRepository.findByExpectedHireDateAndPositionIgnoreCaseAndRegion(
                futureDate, "Developer", Region.SUDESTE)).thenReturn(Optional.empty());
        when(expectedHiringRepository.save(entity)).thenReturn(entity);

        // Act
        ExpectedHiringResponseDTO result = expectedHiringService.create(requestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(responseDTO, result);
        verify(expectedHiringRepository).findByExpectedHireDateAndPositionIgnoreCaseAndRegion(
                futureDate, "Developer", Region.SUDESTE);
        verify(mapperExpectedHiring).toExpectedHiring(requestDTO);
        verify(expectedHiringRepository).save(entity);
        verify(mapperExpectedHiring).toExpectedHiringResponseDTO(entity);
    }

    @Test
    void create_AlreadyExists() {
        // Arrange
        when(expectedHiringRepository.findByExpectedHireDateAndPositionIgnoreCaseAndRegion(
                futureDate, "Developer", Region.SUDESTE)).thenReturn(Optional.of(entity));

        // Act & Assert
        ExpectedHiringAlreadyExistsException exception = assertThrows(
                ExpectedHiringAlreadyExistsException.class,
                () -> expectedHiringService.create(requestDTO)
        );
        assertEquals("Expected hiring already exists with ID 1, status CREATED, requirements: Notebook, Mouse, Keyboard",
                exception.getMessage());
        verify(expectedHiringRepository).findByExpectedHireDateAndPositionIgnoreCaseAndRegion(
                futureDate, "Developer", Region.SUDESTE);
        verify(mapperExpectedHiring, never()).toExpectedHiring(any());
        verify(expectedHiringRepository, never()).save(any());
    }

    @Test
    void getExpectedHiringById_Success() {
        // Arrange
        when(expectedHiringRepository.findById(1L)).thenReturn(Optional.of(entity));

        // Act
        ExpectedHiringResponseDTO result = expectedHiringService.getExpectedHiringById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(responseDTO, result);
        verify(expectedHiringRepository).findById(1L);
        verify(mapperExpectedHiring).toExpectedHiringResponseDTO(entity);
    }

    @Test
    void getExpectedHiringById_NotFound() {
        // Arrange
        when(expectedHiringRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> expectedHiringService.getExpectedHiringById(1L)
        );
        assertEquals("Expected hiring not found", exception.getMessage());
        verify(expectedHiringRepository).findById(1L);
        verify(mapperExpectedHiring, never()).toExpectedHiringResponseDTO(any());
    }

    @Test
    void getExpectedHiringById_UpdatesStatusIfExpired() {
        // Arrange
        when(expectedHiringRepository.findById(2L)).thenReturn(Optional.of(expiredEntity));
        when(expectedHiringRepository.save(expiredEntity)).thenReturn(expiredEntity);
        when(mapperExpectedHiring.toExpectedHiringResponseDTO(expiredEntity)).thenReturn(
                ExpectedHiringResponseDTO.builder()
                        .id(2L)
                        .expectedHireDate(pastDate)
                        .position("Designer")
                        .equipmentRequirements("Notebook, Graphics Tablet")
                        .region(Region.NORDESTE)
                        .expectedHiringStatus(ExpectedHiringStatus.EXPIRED)
                        .build()
        );

        // Act
        ExpectedHiringResponseDTO result = expectedHiringService.getExpectedHiringById(2L);

        // Assert
        assertNotNull(result);
        assertEquals(ExpectedHiringStatus.EXPIRED, result.expectedHiringStatus());
        verify(expectedHiringRepository).findById(2L);
        verify(expectedHiringRepository).save(expiredEntity);
        assertEquals(ExpectedHiringStatus.EXPIRED, expiredEntity.getExpectedHiringStatus());
    }

    @Test
    void getExpectedHiringById_UpdatesStatusIfProcessedAndExpired() {
        // Arrange
        when(expectedHiringRepository.findById(3L)).thenReturn(Optional.of(processedEntity));
        when(expectedHiringRepository.save(processedEntity)).thenReturn(processedEntity);
        when(mapperExpectedHiring.toExpectedHiringResponseDTO(processedEntity)).thenReturn(
                ExpectedHiringResponseDTO.builder()
                        .id(3L)
                        .expectedHireDate(pastDate)
                        .position("Manager")
                        .equipmentRequirements("Notebook, Phone")
                        .region(Region.CENTRO_OESTE)
                        .expectedHiringStatus(ExpectedHiringStatus.CONCLUDED)
                        .build()
        );

        // Act
        ExpectedHiringResponseDTO result = expectedHiringService.getExpectedHiringById(3L);

        // Assert
        assertNotNull(result);
        assertEquals(ExpectedHiringStatus.CONCLUDED, result.expectedHiringStatus());
        verify(expectedHiringRepository).findById(3L);
        verify(expectedHiringRepository).save(processedEntity);
        assertEquals(ExpectedHiringStatus.CONCLUDED, processedEntity.getExpectedHiringStatus());
    }

    @Test
    void update_Success() {
        // Arrange
        var updatedEntity = ExpectedHiringEntity.builder()
                .id(1L)
                .expectedHireDate(LocalDate.of(2025, 12, 31))
                .position("Developer")
                .equipmentRequirements("Notebook, Celular")
                .region(Region.SUDESTE)
                .expectedHiringStatus(ExpectedHiringStatus.CREATED)
                .build();

        var updatedResponseDTO = ExpectedHiringResponseDTO.builder()
                .id(1L)
                .expectedHireDate(LocalDate.of(2025, 12, 31))
                .position("Developer")
                .equipmentRequirements("Notebook")
                .region(Region.SUDESTE)
                .expectedHiringStatus(ExpectedHiringStatus.CREATED)
                .build();

        when(expectedHiringRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(expectedHiringRepository.findByExpectedHireDateAndPositionIgnoreCaseAndRegionAndIdNot(
                futureDate, "Developer", Region.SUDESTE, 1L)).thenReturn(Optional.empty());
        when(mapperExpectedHiring.toApplyUpdates(entity, requestDTO)).thenReturn(updatedEntity);
        when(expectedHiringRepository.save(updatedEntity)).thenReturn(updatedEntity);
        when(mapperExpectedHiring.toExpectedHiringResponseDTO(updatedEntity)).thenReturn(updatedResponseDTO);

        // Act
        ExpectedHiringResponseDTO result = expectedHiringService.update(1L, requestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(updatedResponseDTO, result);
        verify(expectedHiringRepository).findById(1L);
        verify(expectedHiringRepository).findByExpectedHireDateAndPositionIgnoreCaseAndRegionAndIdNot(
                futureDate, "Developer", Region.SUDESTE, 1L);
        verify(mapperExpectedHiring).toApplyUpdates(entity, requestDTO);
        verify(expectedHiringRepository).save(updatedEntity);
        verify(mapperExpectedHiring).toExpectedHiringResponseDTO(updatedEntity);
    }


    @Test
    void update_NotFound() {
        // Arrange
        when(expectedHiringRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> expectedHiringService.update(1L, requestDTO)
        );
        assertEquals("Expected hiring not found", exception.getMessage());
        verify(expectedHiringRepository).findById(1L);
        verify(expectedHiringRepository, never()).findByExpectedHireDateAndPositionIgnoreCaseAndRegionAndIdNot(
                any(), any(), any(), anyLong());
        verify(mapperExpectedHiring, never()).toApplyUpdates(any(), any());
        verify(expectedHiringRepository, never()).save(any());
    }

    @Test
    void update_DuplicateExists() {
        // Arrange
        when(i18nUtil.getMessage("expected.hiring.already.exists", 2L))
                .thenReturn("Expected hiring already exists with ID 2");
        when(expectedHiringRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(expectedHiringRepository.findByExpectedHireDateAndPositionIgnoreCaseAndRegionAndIdNot(
                futureDate, "Developer", Region.SUDESTE, 1L)).thenReturn(Optional.of(
                ExpectedHiringEntity.builder()
                        .id(1L)
                        .expectedHireDate(futureDate)
                        .position("Developer")
                        .equipmentRequirements("Notebook, Mouse")
                        .region(Region.SUDESTE)
                        .expectedHiringStatus(ExpectedHiringStatus.CREATED)
                        .build()
        ));

        // Act & Assert
        ExpectedHiringAlreadyExistsException exception = assertThrows(
                ExpectedHiringAlreadyExistsException.class,
                () -> expectedHiringService.update(1L, requestDTO)
        );
        assertEquals("Expected hiring already exists with ID 1", exception.getMessage());
        verify(expectedHiringRepository).findById(1L);
        verify(expectedHiringRepository).findByExpectedHireDateAndPositionIgnoreCaseAndRegionAndIdNot(
                futureDate, "Developer", Region.SUDESTE, 1L);
        verify(mapperExpectedHiring, never()).toApplyUpdates(any(), any());
        verify(expectedHiringRepository, never()).save(any());
    }

    @Test
    void update_Unchanged() {
        // Arrange
        when(expectedHiringRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(expectedHiringRepository.findByExpectedHireDateAndPositionIgnoreCaseAndRegionAndIdNot(
                futureDate, "Developer", Region.SUDESTE, 1L)).thenReturn(Optional.empty());
        when(mapperExpectedHiring.toApplyUpdates(entity, requestDTO)).thenReturn(entity);

        // Act
        ExpectedHiringResponseDTO result = expectedHiringService.update(1L, requestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(responseDTO, result);
        verify(expectedHiringRepository).findById(1L);
        verify(expectedHiringRepository).findByExpectedHireDateAndPositionIgnoreCaseAndRegionAndIdNot(
                futureDate, "Developer", Region.SUDESTE, 1L);
        verify(mapperExpectedHiring).toApplyUpdates(entity, requestDTO);
        verify(expectedHiringRepository, never()).save(any());
        verify(mapperExpectedHiring).toExpectedHiringResponseDTO(entity);
    }

    @Test
    void delete_Success() {
        // Arrange
        when(expectedHiringRepository.findById(1L)).thenReturn(Optional.of(entity));

        // Act
        expectedHiringService.delete(1L);

        // Assert
        verify(expectedHiringRepository).findById(1L);
        verify(expectedHiringRepository).delete(entity);
    }

    @Test
    void delete_NotFound() {
        // Arrange
        when(expectedHiringRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> expectedHiringService.delete(1L)
        );
        assertEquals("Expected hiring not found", exception.getMessage());
        verify(expectedHiringRepository).findById(1L);
        verify(expectedHiringRepository, never()).delete(any());
    }

    @Test
    void getAllExpectedHirings_Success() {
        // Arrange
        List<ExpectedHiringEntity> entities = Arrays.asList(entity, expiredEntity, processedEntity);
        when(expectedHiringRepository.findAll(any(Sort.class))).thenReturn(entities);

        // Act
        List<ExpectedHiringResponseDTO> results = expectedHiringService.getAllExpectedHirings();

        // Assert
        assertNotNull(results);
        assertEquals(3, results.size());
        verify(expectedHiringRepository).findAll(any(Sort.class));
        verify(mapperExpectedHiring).toExpectedHiringList(entities);

        // Verify status updates
        assertEquals(ExpectedHiringStatus.EXPIRED, expiredEntity.getExpectedHiringStatus());
        assertEquals(ExpectedHiringStatus.CONCLUDED, processedEntity.getExpectedHiringStatus());
        verify(expectedHiringRepository, times(2)).save(any(ExpectedHiringEntity.class));
    }

    @Test
    void markAsProcessed_Success() {
        // Arrange
        when(expectedHiringRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(expectedHiringRepository.save(entity)).thenReturn(entity);

        // Act
        expectedHiringService.markAsProcessed(1L);

        // Assert
        verify(expectedHiringRepository).findById(1L);
        verify(expectedHiringRepository).save(entity);
        assertEquals(ExpectedHiringStatus.PROCESSED, entity.getExpectedHiringStatus());
    }

    @Test
    void markAsProcessed_NotFound() {
        // Arrange
        when(expectedHiringRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> expectedHiringService.markAsProcessed(1L)
        );
        assertEquals("Expected hiring not found", exception.getMessage());
        verify(expectedHiringRepository).findById(1L);
        verify(expectedHiringRepository, never()).save(any());
    }

    @Test
    void markAsProcessed_InvalidStatus() {
        // Arrange
        ExpectedHiringEntity processedEntity = ExpectedHiringEntity.builder()
                .id(1L)
                .expectedHireDate(futureDate)
                .position("Developer")
                .equipmentRequirements("Notebook, Mouse, Keyboard")
                .region(Region.SUDESTE)
                .expectedHiringStatus(ExpectedHiringStatus.PROCESSED)
                .build();

        when(expectedHiringRepository.findById(1L)).thenReturn(Optional.of(processedEntity));

        // Act & Assert
        InvalidStatusException exception = assertThrows(
                InvalidStatusException.class,
                () -> expectedHiringService.markAsProcessed(1L)
        );
        assertEquals("Invalid status for expected hiring", exception.getMessage());
        verify(expectedHiringRepository).findById(1L);
        verify(expectedHiringRepository, never()).save(any());
    }
}
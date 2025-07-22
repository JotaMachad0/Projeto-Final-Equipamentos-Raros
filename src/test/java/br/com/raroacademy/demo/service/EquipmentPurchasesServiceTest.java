package br.com.raroacademy.demo.service;

import br.com.raroacademy.demo.commons.i18n.I18nUtil;
import br.com.raroacademy.demo.domain.DTO.equipment.purchase.EquipmentPurchasesRequestDTO;
import br.com.raroacademy.demo.domain.DTO.equipment.purchase.EquipmentPurchasesResponseDTO;
import br.com.raroacademy.demo.domain.DTO.equipment.purchase.MapperEquipmentPurchases;
import br.com.raroacademy.demo.domain.entities.EquipmentPurchasesEntity;
import br.com.raroacademy.demo.domain.enums.EquipmentType;
import br.com.raroacademy.demo.domain.enums.PurchaseStatus;
import br.com.raroacademy.demo.exception.NotFoundException;
import br.com.raroacademy.demo.repository.EquipmentPurchasesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class EquipmentPurchasesServiceTest {

    @Mock
    private EquipmentPurchasesRepository equipmentPurchasesRepository;

    @Mock
    private MapperEquipmentPurchases mapper;
    
    @Mock
    private I18nUtil i18n;

    @InjectMocks
    private EquipmentPurchasesService equipmentPurchasesService;

    private EquipmentPurchasesEntity purchaseEntity;
    private EquipmentPurchasesRequestDTO purchaseRequestDTO;
    private EquipmentPurchasesResponseDTO purchaseResponseDTO;
    private LocalDate orderDate;
    private LocalDate receiptDate;

    @BeforeEach
    void setUp() {
        orderDate = LocalDate.of(2025, 7, 19);
        receiptDate = LocalDate.of(2025, 7, 29);

        purchaseEntity = EquipmentPurchasesEntity.builder()
                .id(1L)
                .equipmentType(EquipmentType.NOTEBOOK)
                .quantity(10)
                .orderDate(orderDate)
                .receiptDate(receiptDate)
                .supplier("Tech Supplies Inc.")
                .status(PurchaseStatus.PURCHASED)
                .build();

        purchaseRequestDTO = new EquipmentPurchasesRequestDTO(
                EquipmentType.NOTEBOOK,
                10,
                orderDate,
                "Tech Supplies Inc.",
                receiptDate,
                PurchaseStatus.PURCHASED
        );

        purchaseResponseDTO = EquipmentPurchasesResponseDTO.builder()
                .id(1L)
                .equipmentType(EquipmentType.NOTEBOOK)
                .quantity(10)
                .orderDate(orderDate)
                .receiptDate(receiptDate)
                .supplier("Tech Supplies Inc.")
                .status(PurchaseStatus.PURCHASED)
                .build();
        

        when(i18n.getMessage("purchase.not.found")).thenReturn("Purchase not found.");
    }

    @Test
    void create_Success() {
        // Arrange
        when(mapper.toEntity(any(EquipmentPurchasesRequestDTO.class))).thenReturn(purchaseEntity);
        when(equipmentPurchasesRepository.save(purchaseEntity)).thenReturn(purchaseEntity);
        when(mapper.toResponseDTO(purchaseEntity)).thenReturn(purchaseResponseDTO);

        // Act
        EquipmentPurchasesResponseDTO result = equipmentPurchasesService.create(purchaseRequestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(purchaseResponseDTO, result);
        verify(mapper).toEntity(any(EquipmentPurchasesRequestDTO.class));
        verify(equipmentPurchasesRepository).save(purchaseEntity);
        verify(mapper).toResponseDTO(purchaseEntity);
    }

    @Test
    void getById_Success() {
        // Arrange
        when(equipmentPurchasesRepository.findById(1L)).thenReturn(Optional.of(purchaseEntity));
        when(mapper.toResponseDTO(purchaseEntity)).thenReturn(purchaseResponseDTO);

        // Act
        EquipmentPurchasesResponseDTO result = equipmentPurchasesService.getById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(purchaseResponseDTO, result);
        verify(equipmentPurchasesRepository).findById(1L);
        verify(mapper).toResponseDTO(purchaseEntity);
    }

    @Test
    void getById_NotFound() {
        // Arrange
        when(equipmentPurchasesRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> equipmentPurchasesService.getById(1L)
        );
        assertEquals("Purchase not found.", exception.getMessage());
        verify(equipmentPurchasesRepository).findById(1L);
        verify(mapper, never()).toResponseDTO(any());
    }

    @Test
    void getAll_Success() {
        // Arrange
        List<EquipmentPurchasesEntity> purchaseEntities = Arrays.asList(purchaseEntity);
        List<EquipmentPurchasesResponseDTO> purchaseResponseDTOs = Arrays.asList(purchaseResponseDTO);

        when(equipmentPurchasesRepository.findAll()).thenReturn(purchaseEntities);
        when(mapper.toResponseDTOList(purchaseEntities)).thenReturn(purchaseResponseDTOs);

        // Act
        List<EquipmentPurchasesResponseDTO> result = equipmentPurchasesService.getAll();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(purchaseResponseDTO, result.get(0));
        verify(equipmentPurchasesRepository).findAll();
        verify(mapper).toResponseDTOList(purchaseEntities);
    }

    @Test
    void update_Success() {
        // Arrange
        when(equipmentPurchasesRepository.findById(1L)).thenReturn(Optional.of(purchaseEntity));
        when(equipmentPurchasesRepository.save(purchaseEntity)).thenReturn(purchaseEntity);
        when(mapper.toResponseDTO(purchaseEntity)).thenReturn(purchaseResponseDTO);

        // Act
        EquipmentPurchasesResponseDTO result = equipmentPurchasesService.update(1L, purchaseRequestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(purchaseResponseDTO, result);
        verify(equipmentPurchasesRepository).findById(1L);
        verify(mapper).updateEntityFromDTO(purchaseEntity, purchaseRequestDTO);
        verify(equipmentPurchasesRepository).save(purchaseEntity);
        verify(mapper).toResponseDTO(purchaseEntity);
    }

    @Test
    void update_NotFound() {
        // Arrange
        when(equipmentPurchasesRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> equipmentPurchasesService.update(1L, purchaseRequestDTO)
        );
        assertEquals("Purchase not found.", exception.getMessage());
        verify(equipmentPurchasesRepository).findById(1L);
        verify(mapper, never()).updateEntityFromDTO(any(), any());
        verify(equipmentPurchasesRepository, never()).save(any());
        verify(mapper, never()).toResponseDTO(any());
    }

    @Test
    void registerInStock_Success() {
        // Arrange
        when(equipmentPurchasesRepository.findById(1L)).thenReturn(Optional.of(purchaseEntity));
        when(equipmentPurchasesRepository.save(purchaseEntity)).thenReturn(purchaseEntity);

        EquipmentPurchasesResponseDTO registeredResponseDTO = EquipmentPurchasesResponseDTO.builder()
                .id(1L)
                .equipmentType(EquipmentType.NOTEBOOK)
                .quantity(10)
                .orderDate(orderDate)
                .receiptDate(receiptDate)
                .supplier("Tech Supplies Inc.")
                .status(PurchaseStatus.REGISTERED)
                .build();

        when(mapper.toResponseDTO(purchaseEntity)).thenReturn(registeredResponseDTO);

        // Act
        EquipmentPurchasesResponseDTO result = equipmentPurchasesService.registerInStock(1L);

        // Assert
        assertNotNull(result);
        assertEquals(registeredResponseDTO, result);
        assertEquals(PurchaseStatus.REGISTERED, purchaseEntity.getStatus());
        verify(equipmentPurchasesRepository).findById(1L);
        verify(equipmentPurchasesRepository).save(purchaseEntity);
        verify(mapper).toResponseDTO(purchaseEntity);
    }

    @Test
    void registerInStock_NotFound() {
        // Arrange
        when(equipmentPurchasesRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> equipmentPurchasesService.registerInStock(1L)
        );
        assertEquals("Purchase not found.", exception.getMessage());
        verify(equipmentPurchasesRepository).findById(1L);
        verify(equipmentPurchasesRepository, never()).save(any());
        verify(mapper, never()).toResponseDTO(any());
    }

}
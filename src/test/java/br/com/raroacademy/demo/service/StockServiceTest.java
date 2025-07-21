package br.com.raroacademy.demo.service;

import br.com.raroacademy.demo.commons.i18n.I18nUtil;
import br.com.raroacademy.demo.domain.DTO.stock.MapperStock;
import br.com.raroacademy.demo.domain.DTO.stock.StockRequestDTO;
import br.com.raroacademy.demo.domain.DTO.stock.StockResponseDTO;
import br.com.raroacademy.demo.domain.entities.StockEntity;
import br.com.raroacademy.demo.domain.enums.EquipmentStatus;
import br.com.raroacademy.demo.domain.enums.EquipmentType;
import br.com.raroacademy.demo.exception.NotFoundException;
import br.com.raroacademy.demo.repository.EquipmentRepository;
import br.com.raroacademy.demo.repository.StockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class StockServiceTest {

    @Mock
    private StockRepository stockRepository;

    @Mock
    private EquipmentRepository equipmentRepository;

    @Mock
    private I18nUtil i18n;

    @Mock
    private MapperStock mapperStock;

    @Mock
    private StockAlertService stockAlertService;

    @InjectMocks
    private StockService stockService;

    private StockEntity stockEntity;
    private StockRequestDTO stockRequestDTO;
    private StockResponseDTO stockResponseDTO;

    @BeforeEach
    void setUp() {
        stockEntity = StockEntity.builder()
                .id(1L)
                .equipmentType(EquipmentType.NOTEBOOK)
                .minStock(5)
                .securityStock(10)
                .currentStock(15)
                .avgRestockTimeDays(30)
                .avgStockConsumptionTimeDays(60)
                .avgDefectiveRate(0.05f)
                .build();

        stockRequestDTO = new StockRequestDTO(
                5,
                10
        );

        stockResponseDTO = StockResponseDTO.builder()
                .id(1L)
                .equipmentType(EquipmentType.NOTEBOOK)
                .minStock(5)
                .securityStock(10)
                .currentStock(15)
                .avgRestockTimeDays(30)
                .avgStockConsumptionTimeDays(60)
                .avgDefectiveRate(0.05f)
                .build();

        when(i18n.getMessage(eq("stock.not.found.for.type"), any())).thenReturn("Stock not found for type: NOTEBOOK");
        when(i18n.getMessage("stock.negative.value")).thenReturn("Stock cannot be negative");
        when(i18n.getMessage(eq("equipmenttype.notebook"))).thenReturn("Notebook");
        when(i18n.getMessage(eq("stock.unchanged"), any())).thenReturn("Stock unchanged for Notebook");
    }

    @Test
    void updateByEquipmentType_Success() {
        // Arrange
        when(stockRepository.findByEquipmentType(EquipmentType.NOTEBOOK)).thenReturn(stockEntity);

        StockEntity updatedEntity = StockEntity.builder()
                .id(1L)
                .equipmentType(EquipmentType.NOTEBOOK)
                .minStock(7)
                .securityStock(12)
                .currentStock(15)
                .avgRestockTimeDays(30)
                .avgStockConsumptionTimeDays(60)
                .avgDefectiveRate(0.05f)
                .build();

        when(mapperStock.toApplyUpdates(eq(stockEntity), any(StockRequestDTO.class))).thenReturn(updatedEntity);
        when(equipmentRepository.countByType(EquipmentType.NOTEBOOK)).thenReturn(20L);
        when(equipmentRepository.countByTypeAndStatus(EquipmentType.NOTEBOOK, EquipmentStatus.DEFECTIVE)).thenReturn(1L);
        when(stockRepository.save(updatedEntity)).thenReturn(updatedEntity);
        when(mapperStock.toResponseDTO(updatedEntity)).thenReturn(stockResponseDTO);

        // Act
        StockResponseDTO result = stockService.updateByEquipmentType(EquipmentType.NOTEBOOK, stockRequestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(stockResponseDTO, result);
        verify(stockRepository).findByEquipmentType(EquipmentType.NOTEBOOK);
        verify(mapperStock).toApplyUpdates(eq(stockEntity), any(StockRequestDTO.class));
        verify(equipmentRepository).countByType(EquipmentType.NOTEBOOK);
        verify(equipmentRepository).countByTypeAndStatus(EquipmentType.NOTEBOOK, EquipmentStatus.DEFECTIVE);
        verify(stockRepository).save(any(StockEntity.class));
        verify(stockAlertService).checkAndCreateAlert(any(StockEntity.class));
        verify(mapperStock).toResponseDTO(any(StockEntity.class));
    }

    @Test
    void updateByEquipmentType_NotFound() {
        // Arrange
        when(stockRepository.findByEquipmentType(EquipmentType.NOTEBOOK)).thenReturn(null);

        // Act & Assert
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> stockService.updateByEquipmentType(EquipmentType.NOTEBOOK, stockRequestDTO)
        );
        assertEquals("Stock not found for type: NOTEBOOK", exception.getMessage());
        verify(stockRepository).findByEquipmentType(EquipmentType.NOTEBOOK);
        verify(mapperStock, never()).toApplyUpdates(any(), any());
        verify(stockRepository, never()).save(any());
    }

    @Test
    void updateByEquipmentType_Unchanged() {
        // Arrange
        when(stockRepository.findByEquipmentType(EquipmentType.NOTEBOOK)).thenReturn(stockEntity);
        when(mapperStock.toApplyUpdates(eq(stockEntity), any(StockRequestDTO.class))).thenReturn(stockEntity);
        when(equipmentRepository.countByType(EquipmentType.NOTEBOOK)).thenReturn(20L);
        when(equipmentRepository.countByTypeAndStatus(EquipmentType.NOTEBOOK, EquipmentStatus.DEFECTIVE)).thenReturn(1L);
        when(mapperStock.toResponseDTO(stockEntity)).thenReturn(stockResponseDTO);

        doAnswer(invocation -> {
            StockEntity entity = invocation.getArgument(0);
            StockRequestDTO dto = invocation.getArgument(1);
            return entity;
        }).when(mapperStock).toApplyUpdates(eq(stockEntity), any(StockRequestDTO.class));

        // Act
        StockResponseDTO result = stockService.updateByEquipmentType(EquipmentType.NOTEBOOK, stockRequestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(stockResponseDTO, result);
        verify(stockRepository).findByEquipmentType(EquipmentType.NOTEBOOK);
        verify(mapperStock).toApplyUpdates(eq(stockEntity), any(StockRequestDTO.class));
        verify(equipmentRepository).countByType(EquipmentType.NOTEBOOK);
        verify(equipmentRepository).countByTypeAndStatus(EquipmentType.NOTEBOOK, EquipmentStatus.DEFECTIVE);
        verify(stockRepository, never()).save(any());
        verify(stockAlertService, never()).checkAndCreateAlert(any());
        verify(mapperStock).toResponseDTO(stockEntity);
    }

    @Test
    void findByEquipmentType_Success() {
        // Arrange
        when(stockRepository.findByEquipmentType(EquipmentType.NOTEBOOK)).thenReturn(stockEntity);
        when(mapperStock.toResponseDTO(stockEntity)).thenReturn(stockResponseDTO);

        // Act
        StockResponseDTO result = stockService.findByEquipmentType(EquipmentType.NOTEBOOK);

        // Assert
        assertNotNull(result);
        assertEquals(stockResponseDTO, result);
        verify(stockRepository).findByEquipmentType(EquipmentType.NOTEBOOK);
        verify(mapperStock).toResponseDTO(stockEntity);
    }

    @Test
    void findByEquipmentType_NotFound() {
        // Arrange
        when(stockRepository.findByEquipmentType(EquipmentType.NOTEBOOK)).thenReturn(null);

        // Act & Assert
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> stockService.findByEquipmentType(EquipmentType.NOTEBOOK)
        );
        assertEquals("Stock not found for type: NOTEBOOK", exception.getMessage());
        verify(stockRepository).findByEquipmentType(EquipmentType.NOTEBOOK);
        verify(mapperStock, never()).toResponseDTO(any());
    }

    @Test
    void incrementStock_Success() {
        // Arrange
        when(stockRepository.findByEquipmentType(EquipmentType.NOTEBOOK)).thenReturn(stockEntity);
        when(stockRepository.save(stockEntity)).thenReturn(stockEntity);

        // Act
        stockService.incrementStock(EquipmentType.NOTEBOOK);

        // Assert
        assertEquals(16, stockEntity.getCurrentStock()); // 15 + 1
        verify(stockRepository).findByEquipmentType(EquipmentType.NOTEBOOK);
        verify(stockRepository).save(stockEntity);
    }

    @Test
    void incrementStock_NotFound() {
        // Arrange
        when(stockRepository.findByEquipmentType(EquipmentType.NOTEBOOK)).thenReturn(null);

        // Act & Assert
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> stockService.incrementStock(EquipmentType.NOTEBOOK)
        );
        assertEquals("Stock not found for type: NOTEBOOK", exception.getMessage());
        verify(stockRepository).findByEquipmentType(EquipmentType.NOTEBOOK);
        verify(stockRepository, never()).save(any());
    }

    @Test
    void decrementStock_Success() {
        // Arrange
        when(stockRepository.findByEquipmentType(EquipmentType.NOTEBOOK)).thenReturn(stockEntity);
        when(stockRepository.save(stockEntity)).thenReturn(stockEntity);

        // Act
        stockService.decrementStock(EquipmentType.NOTEBOOK);

        // Assert
        assertEquals(14, stockEntity.getCurrentStock()); // 15 - 1
        verify(stockRepository).findByEquipmentType(EquipmentType.NOTEBOOK);
        verify(stockRepository).save(stockEntity);
        verify(stockAlertService).checkAndCreateAlert(stockEntity);
    }

    @Test
    void decrementStock_NotFound() {
        // Arrange
        when(stockRepository.findByEquipmentType(EquipmentType.NOTEBOOK)).thenReturn(null);

        // Act & Assert
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> stockService.decrementStock(EquipmentType.NOTEBOOK)
        );
        assertEquals("Stock not found for type: NOTEBOOK", exception.getMessage());
        verify(stockRepository).findByEquipmentType(EquipmentType.NOTEBOOK);
        verify(stockRepository, never()).save(any());
        verify(stockAlertService, never()).checkAndCreateAlert(any());
    }

    @Test
    void decrementStock_NegativeStock() {
        // Arrange
        stockEntity.setCurrentStock(0);
        when(stockRepository.findByEquipmentType(EquipmentType.NOTEBOOK)).thenReturn(stockEntity);

        // Act & Assert
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> stockService.decrementStock(EquipmentType.NOTEBOOK)
        );
        assertEquals("Stock cannot be negative", exception.getMessage());
        verify(stockRepository).findByEquipmentType(EquipmentType.NOTEBOOK);
        verify(stockRepository, never()).save(any());
        verify(stockAlertService, never()).checkAndCreateAlert(any());
    }

    @Test
    void getAllStocks_Success() {
        // Arrange
        List<StockEntity> stockEntities = Arrays.asList(stockEntity);
        List<StockResponseDTO> stockResponseDTOs = Arrays.asList(stockResponseDTO);

        when(stockRepository.findAll(any(Sort.class))).thenReturn(stockEntities);
        when(mapperStock.toStockList(stockEntities)).thenReturn(stockResponseDTOs);

        // Act
        List<StockResponseDTO> result = stockService.getAllStocks();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(stockResponseDTO, result.get(0));
        verify(stockRepository).findAll(any(Sort.class));
        verify(mapperStock).toStockList(stockEntities);
    }

    @Test
    void getAllStocks_EmptyList() {
        // Arrange
        when(stockRepository.findAll(any(Sort.class))).thenReturn(List.of());
        when(mapperStock.toStockList(List.of())).thenReturn(List.of());

        // Act
        List<StockResponseDTO> result = stockService.getAllStocks();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(stockRepository).findAll(any(Sort.class));
        verify(mapperStock).toStockList(List.of());
    }
}
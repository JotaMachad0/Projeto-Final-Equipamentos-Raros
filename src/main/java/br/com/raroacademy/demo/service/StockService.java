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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;
    private final EquipmentRepository equipmentRepository;
    private final I18nUtil i18n;
    private final MapperStock mapperStock;
    private final StockAlertService stockAlertService;

    @Transactional
    public StockResponseDTO updateByEquipmentType(EquipmentType equipmentType, StockRequestDTO request) {
        var existing = stockRepository.findByEquipmentType(equipmentType);
        if (existing == null) {
            throw new NotFoundException(i18n.getMessage("stock.not.found.for.type", equipmentType.name()));
        }

        var original = StockEntity.builder()
                .id(existing.getId())
                .equipmentType(existing.getEquipmentType())
                .minStock(existing.getMinStock())
                .securityStock(existing.getSecurityStock())
                .currentStock(existing.getCurrentStock())
                .avgRestockTimeDays(existing.getAvgRestockTimeDays())
                .avgStockConsumptionTimeDays(existing.getAvgStockConsumptionTimeDays())
                .avgDefectiveRate(existing.getAvgDefectiveRate())
                .build();

        var updated = mapperStock.toApplyUpdates(existing, request);
        var totalEquipments = equipmentRepository.countByType(equipmentType);
        var defectiveEquipments = equipmentRepository.countByTypeAndStatus(equipmentType, EquipmentStatus.DEFECTIVE);
        var avgDefectiveRate = totalEquipments > 0 ? (float) defectiveEquipments / totalEquipments : 0.0f;
        updated.setAvgDefectiveRate(avgDefectiveRate);

        if (updated.equals(original)) {
            String translatedLabel = i18n.getMessage("equipmenttype." + equipmentType.name().toLowerCase());
            log.info(i18n.getMessage("stock.unchanged", translatedLabel));
            return mapperStock.toResponseDTO(existing);
        }

        var saved = stockRepository.save(updated);
        stockAlertService.checkAndCreateAlert(saved);
        return mapperStock.toResponseDTO(saved);
    }
    @Transactional
    public StockResponseDTO findByEquipmentType(EquipmentType equipmentType) {
        var stock = stockRepository.findByEquipmentType(equipmentType);
        if (stock == null) {
            throw new IllegalStateException(i18n.getMessage("stock.not.found.for.type", equipmentType.name()));
        }
        return mapperStock.toResponseDTO(stock);
    }
    @Transactional
    public void incrementStock(EquipmentType equipmentType) {
        var stock = stockRepository.findByEquipmentType(equipmentType);
        if (stock == null) {
            throw new IllegalStateException(i18n.getMessage("stock.not.found.for.type", equipmentType.name()));
        }
        stock.setCurrentStock(stock.getCurrentStock() + 1);

        var totalEquipments = equipmentRepository.countByType(equipmentType);
        var defectiveEquipments = equipmentRepository.countByTypeAndStatus(equipmentType, EquipmentStatus.DEFECTIVE);
        var avgDefectiveRate = totalEquipments > 0 ? (float) defectiveEquipments / totalEquipments : 0.0f;
        stock.setAvgDefectiveRate(avgDefectiveRate);

        stockRepository.save(stock);
    }
    @Transactional
    public void decrementStock(EquipmentType equipmentType) {
        var stock = stockRepository.findByEquipmentType(equipmentType);
        if (stock == null) {
            throw new IllegalStateException(i18n.getMessage("stock.not.found.for.type", equipmentType.name()));
        }
        stock.setCurrentStock(stock.getCurrentStock() - 1);
        if (stock.getCurrentStock() < 0) {
            throw new IllegalStateException(i18n.getMessage("stock.negative.value"));
        }

        var totalEquipments = equipmentRepository.countByType(equipmentType);
        var defectiveEquipments = equipmentRepository.countByTypeAndStatus(equipmentType, EquipmentStatus.DEFECTIVE);
        var avgDefectiveRate = totalEquipments > 0 ? (float) defectiveEquipments / totalEquipments : 0.0f;
        stock.setAvgDefectiveRate(avgDefectiveRate);

        stockRepository.save(stock);
        stockAlertService.checkAndCreateAlert(stock);
    }
    @Transactional
    public void calculateStock(EquipmentType equipmentType) {
        var stock = stockRepository.findByEquipmentType(equipmentType);
        if (stock == null) {
            throw new IllegalStateException(i18n.getMessage("stock.not.found.for.type", equipmentType.name()));
        }
        var totalEquipments = equipmentRepository.countByType(equipmentType);
        var defectiveEquipments = equipmentRepository.countByTypeAndStatus(equipmentType, EquipmentStatus.DEFECTIVE);
        var avgDefectiveRate = totalEquipments > 0 ? (float) defectiveEquipments / totalEquipments : 0.0f;
        stock.setAvgDefectiveRate(avgDefectiveRate);

        stockRepository.save(stock);
    }

    @Transactional(readOnly = true)
    public List<StockResponseDTO> getAllStocks() {
        var stockList = stockRepository.findAll(
                Sort.by(Sort.Direction.ASC, "equipmentType")
        );
        return mapperStock.toStockList(stockList);
    }
}

package br.com.raroacademy.demo.service;

import br.com.raroacademy.demo.commons.i18n.I18nUtil;
import br.com.raroacademy.demo.domain.DTO.stock.MapperStock;
import br.com.raroacademy.demo.domain.DTO.stock.StockResponseDTO;
import br.com.raroacademy.demo.domain.DTO.stock.UpdateStockRequestDTO;
import br.com.raroacademy.demo.domain.enums.EquipmentStatus;
import br.com.raroacademy.demo.domain.enums.EquipmentType;
import br.com.raroacademy.demo.repository.EquipmentRepository;
import br.com.raroacademy.demo.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;
    private final EquipmentRepository equipmentRepository;
    private final I18nUtil i18n;

    public StockResponseDTO updateByEquipmentType(EquipmentType equipmentType, UpdateStockRequestDTO request) {
        var stock = stockRepository.findByEquipmentType(equipmentType);
        if (stock == null) {
            throw new IllegalStateException(i18n.getMessage("stock.not.found.for.type", equipmentType.name()));
        }

        stock.setMinStock(request.getMinStock());
        stock.setSecurityStock(request.getSecurityStock());
        stock.setAvgRestockTimeDays(request.getAvgRestockTimeDays());
        stock.setAvgStockConsumptionTimeDays(request.getAvgStockConsumptionTimeDays());

        var totalEquipments = equipmentRepository.countByType(equipmentType);
        var defectiveEquipments = equipmentRepository.countByTypeAndStatus(equipmentType, EquipmentStatus.DEFECTIVE);

        var avgDefectiveRate = totalEquipments > 0 ? (float) defectiveEquipments / totalEquipments : 0.0f;
        stock.setAvgDefectiveRate(avgDefectiveRate);

        var updated = stockRepository.save(stock);
        return MapperStock.toResponseDTO(updated);
    }

    public StockResponseDTO findByEquipmentType(EquipmentType equipmentType) {
        var stock = stockRepository.findByEquipmentType(equipmentType);
        if (stock == null) {
            throw new IllegalStateException(i18n.getMessage("stock.not.found.for.type", equipmentType.name()));
        }
        return MapperStock.toResponseDTO(stock);
    }

    public void incrementStock(EquipmentType equipmentType) {
        var stock = stockRepository.findByEquipmentType(equipmentType);
        if (stock == null) {
            throw new IllegalStateException(i18n.getMessage("stock.not.found.for.type", equipmentType.name()));
        }
        stock.setCurrentStock(stock.getCurrentStock() + 1);
        stockRepository.save(stock);
    }

    public void decrementStock(EquipmentType equipmentType) {
        var stock = stockRepository.findByEquipmentType(equipmentType);
        if (stock == null) {
            throw new IllegalStateException(i18n.getMessage("stock.not.found.for.type", equipmentType.name()));
        }
        stock.setCurrentStock(stock.getCurrentStock() - 1);
        if (stock.getCurrentStock() < 0) {
            throw new IllegalStateException(i18n.getMessage("stock.negative.value"));
        }
        stockRepository.save(stock);
    }
}

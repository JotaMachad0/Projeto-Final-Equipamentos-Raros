package br.com.raroacademy.demo.domain.DTO.stock;

import br.com.raroacademy.demo.domain.entities.StockEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MapperStock {

    public StockResponseDTO toResponseDTO(StockEntity entity) {
        return StockResponseDTO.builder()
                .id(entity.getId())
                .equipmentType(entity.getEquipmentType())
                .minStock(entity.getMinStock())
                .securityStock(entity.getSecurityStock())
                .currentStock(entity.getCurrentStock())
                .avgRestockTimeDays(entity.getAvgRestockTimeDays())
                .avgStockConsumptionTimeDays(entity.getAvgStockConsumptionTimeDays())
                .avgDefectiveRate(entity.getAvgDefectiveRate())
                .build();
    }

    public StockEntity toApplyUpdates(StockEntity existing, StockRequestDTO request) {
        return StockEntity.builder()
                .id(existing.getId())
                .equipmentType(existing.getEquipmentType())
                .currentStock(existing.getCurrentStock())
                .securityStock(request.securityStock())
                .minStock(request.minStock())
                .avgRestockTimeDays(existing.getAvgRestockTimeDays())
                .avgStockConsumptionTimeDays(existing.getAvgStockConsumptionTimeDays())
                .avgDefectiveRate(existing.getAvgDefectiveRate())
                .build();
    }

    public List<StockResponseDTO> toStockList(List<StockEntity> list) {
        return list.stream().map(this::toResponseDTO).toList();
    }
}

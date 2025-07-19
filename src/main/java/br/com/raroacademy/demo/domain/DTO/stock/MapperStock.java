package br.com.raroacademy.demo.domain.DTO.stock;

import br.com.raroacademy.demo.domain.entities.StockEntity;
import org.springframework.stereotype.Component;

@Component
public class MapperStock {

    public static StockResponseDTO toResponseDTO(StockEntity entity) {
        return StockResponseDTO.builder()
                .id(entity.getId())
                .minStock(entity.getMinStock())
                .securityStock(entity.getSecurityStock())
                .currentStock(entity.getCurrentStock())
                .avgRestockTimeDays(entity.getAvgRestockTimeDays())
                .avgStockConsumptionTimeDays(entity.getAvgStockConsumptionTimeDays())
                .avgDefectiveRate(entity.getAvgDefectiveRate())
                .build();
    }
}

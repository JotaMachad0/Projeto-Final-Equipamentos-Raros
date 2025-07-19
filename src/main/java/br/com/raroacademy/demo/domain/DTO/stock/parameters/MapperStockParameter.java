package br.com.raroacademy.demo.domain.DTO.stock.parameters;

import br.com.raroacademy.demo.domain.DTO.equipment.EquipmentSummaryDTO;
import br.com.raroacademy.demo.domain.entities.StockParameterEntity;
import org.springframework.stereotype.Component;

@Component
public class MapperStockParameter {

    public static StockParameterResponseDTO toResponseDTO(StockParameterEntity entity) {
        return StockParameterResponseDTO.builder()
                .id(entity.getId())
                .minStock(entity.getMinStock())
                .securityStock(entity.getSecurityStock())
                .currentStock(entity.getCurrentStock())
                .avgRestockTimeDays(entity.getAvgRestockTimeDays())
                .avgStockConsumptionTimeDays(entity.getAvgStockConsumptionTimeDays())
                .avgDefectiveRate(entity.getAvgDefectiveRate())
                .equipment(EquipmentSummaryDTO.builder()
                        .id(entity.getEquipment().getId())
                        .type(entity.getEquipment().getType().name())
                        .model(entity.getEquipment().getModel())
                        .build())
                .build();
    }
}

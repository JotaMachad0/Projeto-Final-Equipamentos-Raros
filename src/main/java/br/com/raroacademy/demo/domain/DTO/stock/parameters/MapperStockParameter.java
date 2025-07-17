package br.com.raroacademy.demo.domain.DTO.stock.parameters;

import br.com.raroacademy.demo.domain.entities.EquipmentEntity;
import br.com.raroacademy.demo.domain.entities.StockParameterEntity;

public class MapperStockParameter {

    public static StockParameterEntity toEntity(StockParameterRequestDTO dto, EquipmentEntity equipment) {
        return StockParameterEntity.builder()
                .equipment(equipment)
                .minStock(dto.getMinStock())
                .avgRestockTimeDays(dto.getAvgRestockTimeDays())
                .avgStockConsumptionTimeDays(dto.getAvgStockConsumptionTimeDays())
                .avgDefectiveRate(dto.getAvgDefectiveRate())
                .build();
    }

    public static StockParameterResponseDTO toResponseDTO(StockParameterEntity entity) {
        return StockParameterResponseDTO.builder()
                .id(entity.getId())
                .equipmentId(entity.getEquipment().getId())
                .minStock(entity.getMinStock())
                .avgRestockTimeDays(entity.getAvgRestockTimeDays())
                .avgStockConsumptionTimeDays(entity.getAvgStockConsumptionTimeDays())
                .avgDeliveryTimeDays(entity.getAvgDeliveryTimeDays())
                .avgDefectiveRate(entity.getAvgDefectiveRate())
                .build();
    }
}

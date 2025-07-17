package br.com.raroacademy.demo.domain.DTO.stock.parameters;

import br.com.raroacademy.demo.domain.entities.StockParameter;

public class MapperStockParameter {

    public static StockParameter toEntity(StockParameterRequestDTO dto) {
        return StockParameter.builder()
                .equipmentType(dto.getEquipmentType())
                .minStock(dto.getMinStock())
                .avgRestockTimeDays(dto.getAvgRestockTimeDays())
                .avgStockConsumptionTimeDays(dto.getAvgStockConsumptionTimeDays())
                .avgDeliveryTimeDays(dto.getAvgDeliveryTimeDays())
                .avgDefectiveRate(Float.valueOf(dto.getAvgDefectiveRate()))
                .build();
    }

    public static StockParameterResponseDTO toResponseDTO(StockParameter entity) {
        return new StockParameterResponseDTO(
                entity.getId(),
                entity.getEquipmentType(),
                entity.getMinStock(),
                entity.getAvgRestockTimeDays(),
                entity.getAvgStockConsumptionTimeDays(),
                entity.getAvgDeliveryTimeDays(),
                entity.getAvgDefectiveRate()
        );
    }
}

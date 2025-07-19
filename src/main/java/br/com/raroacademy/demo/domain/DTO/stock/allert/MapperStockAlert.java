package br.com.raroacademy.demo.domain.DTO.stock.allert;

import br.com.raroacademy.demo.domain.entities.StockAlertEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MapperStockAlert {

    public StockAlertResponseDTO toResponseDTO(StockAlertEntity entity) {
        return new StockAlertResponseDTO(
                entity.getId(),
                entity.getEquipmentStock().getEquipmentType(),
                entity.getEquipmentStock().getCurrentStock(),
                entity.getMinimumStock(),
                entity.getSecurityStock(),
                entity.getAlertSentAt(),
                entity.getStockAlertStatus()
        );
    }

    public List<StockAlertResponseDTO> toStockAlertList(List<StockAlertEntity> list) {
        return list.stream().map(this::toResponseDTO).toList();
    }

}

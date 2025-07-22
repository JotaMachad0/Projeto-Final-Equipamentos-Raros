package br.com.raroacademy.demo.domain.DTO.stock.allert;

import br.com.raroacademy.demo.domain.entities.StockAlertEntity;
import br.com.raroacademy.demo.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MapperStockAlert {

    private final StockRepository stockRepository;

    public StockAlertResponseDTO toResponseDTO(StockAlertEntity entity) {
        var stock = entity.getStock().getEquipmentType();
        var stockEntity = stockRepository.findByEquipmentType(stock);

        return new StockAlertResponseDTO(
                entity.getId(),
                entity.getStock().getEquipmentType(),
                entity.getStock().getCurrentStock(),
                entity.getStock().getSecurityStock(),
                entity.getAlertSentAt(),
                entity.getStockAlertStatus()
        );
    }

    public List<StockAlertResponseDTO> toStockAlertList(List<StockAlertEntity> list) {
        return list.stream().map(this::toResponseDTO).toList();
    }

}

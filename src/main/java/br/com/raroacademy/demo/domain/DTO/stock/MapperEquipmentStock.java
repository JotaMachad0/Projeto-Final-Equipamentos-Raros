package br.com.raroacademy.demo.domain.DTO.stock;

import br.com.raroacademy.demo.domain.entities.EquipmentStockEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MapperEquipmentStock {
    public EquipmentStockEntity toEquipmentStock(EquipmentStockRequestDTO request) {
        return EquipmentStockEntity.builder()
                .equipmentType(request.equipmentType())
                .currentStock(request.currentStock())
                .build();
    }
    
    public EquipmentStockResponseDTO toResponseDTO(EquipmentStockEntity entity) {
        return EquipmentStockResponseDTO.builder()
                .id(entity.getId())
                .equipmentType(entity.getEquipmentType())
                .currentStock(entity.getCurrentStock())
                .build();
    }

    public EquipmentStockEntity toApplyUpdates(EquipmentStockEntity existing, EquipmentStockRequestDTO request) {
        return EquipmentStockEntity.builder()
                .id(existing.getId())
                .equipmentType(request.equipmentType())
                .currentStock(request.currentStock())
                .build();
    }

    public List<EquipmentStockResponseDTO> toEquipmentStockList(List<EquipmentStockEntity> list) {
        return list.stream().map(this::toResponseDTO).toList();
    }
}

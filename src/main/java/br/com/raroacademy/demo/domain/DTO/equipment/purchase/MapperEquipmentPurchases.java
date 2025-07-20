package br.com.raroacademy.demo.domain.DTO.equipment.purchase;
import br.com.raroacademy.demo.domain.entities.EquipmentPurchasesEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MapperEquipmentPurchases {

    public EquipmentPurchasesEntity toEntity(EquipmentPurchasesRequestDTO request) {
        return EquipmentPurchasesEntity.builder()
                .equipmentType(request.equipmentType())
                .quantity(request.quantity())
                .orderDate(request.orderDate())
                .supplier(request.supplier())
                .receiptDate(request.receiptDate())
                .status(request.status())
                .build();
    }

    public EquipmentPurchasesResponseDTO toResponseDTO(EquipmentPurchasesEntity entity) {
        return EquipmentPurchasesResponseDTO.builder()
                .id(entity.getId())
                .equipmentType(entity.getEquipmentType())
                .quantity(entity.getQuantity())
                .orderDate(entity.getOrderDate())
                .supplier(entity.getSupplier())
                .receiptDate(entity.getReceiptDate())
                .status(entity.getStatus())
                .build();
    }

    public void updateEntityFromDTO(EquipmentPurchasesEntity entity, EquipmentPurchasesRequestDTO dto) {
        entity.setEquipmentType(dto.equipmentType());
        entity.setQuantity(dto.quantity());
        entity.setOrderDate(dto.orderDate());
        entity.setSupplier(dto.supplier());
        entity.setReceiptDate(dto.receiptDate());
        entity.setStatus(dto.status());
    }

    public List<EquipmentPurchasesResponseDTO> toResponseDTOList(List<EquipmentPurchasesEntity> entityList) {
        return entityList.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }
}

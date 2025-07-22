package br.com.raroacademy.demo.domain.DTO.equipment.returns;

import br.com.raroacademy.demo.domain.DTO.equipment.collaborator.EquipmentCollaboratorResponseDTO;
import br.com.raroacademy.demo.domain.DTO.equipment.collaborator.MapperEquipmentCollaborator;
import br.com.raroacademy.demo.domain.entities.EquipmentCollaboratorEntity;
import br.com.raroacademy.demo.domain.entities.EquipmentReturnEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class MapperEquipmentReturn {

    private final MapperEquipmentCollaborator mapperEquipmentCollaborator;

    public EquipmentReturnEntity toEntity(EquipmentReturnRequestDTO dto, EquipmentCollaboratorEntity equipmentCollaborator) {
        return EquipmentReturnEntity.builder()
                .deliveryDate(dto.deliveryDate())
                .receiptDate(dto.receiptDate())
                .status(dto.status())
                .note(dto.note())
                .equipmentCollaborator(equipmentCollaborator)
                .build();
    }

    public EquipmentReturnResponseDTO toResponse(EquipmentReturnEntity entity, EquipmentCollaboratorResponseDTO equipmentCollaboratorResponse) {
        return EquipmentReturnResponseDTO.builder()
                .id(entity.getId())
                .deliveryDate(entity.getDeliveryDate())
                .receiptDate(entity.getReceiptDate())
                .status(entity.getStatus())
                .note(entity.getNote())
                .equipmentCollaborator(equipmentCollaboratorResponse)
                .build();
    }

    public EquipmentReturnResponseDTO mapToResponseDTO(EquipmentReturnEntity entity) {
        var equipmentCollaboratorResponse = mapperEquipmentCollaborator.mapToResponseDTO(entity.getEquipmentCollaborator());
        return toResponse(entity, equipmentCollaboratorResponse);
    }
}
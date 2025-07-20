package br.com.raroacademy.demo.domain.DTO.equipment.collaborator;

import br.com.raroacademy.demo.domain.DTO.collaborator.CollaboratorSummaryDTO;
import br.com.raroacademy.demo.domain.DTO.collaborator.MapperCollaborator;
import br.com.raroacademy.demo.domain.DTO.equipment.EquipmentSummaryDTO;
import br.com.raroacademy.demo.domain.DTO.equipment.MapperEquipment;
import br.com.raroacademy.demo.domain.entities.CollaboratorEntity;
import br.com.raroacademy.demo.domain.entities.EquipmentCollaboratorEntity;
import br.com.raroacademy.demo.domain.entities.EquipmentEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class MapperEquipmentCollaborator {
    private final MapperCollaborator mapperCollaborator;
    private final MapperEquipment mapperEquipment;

    public EquipmentCollaboratorEntity toEntity(EquipmentCollaboratorRequestDTO dto,
                                                CollaboratorEntity collaborator,
                                                EquipmentEntity equipment) {
        return EquipmentCollaboratorEntity.builder()
                .collaborator(collaborator)
                .equipment(equipment)
                .deliveryDate(dto.deliveryDate())
                .returnDate(dto.returnDate())
                .deliveryStatus(dto.deliveryStatus())
                .notes(dto.notes())
                .build();
    }

    public EquipmentCollaboratorResponseDTO toResponse(EquipmentCollaboratorEntity entity,
                                                       CollaboratorSummaryDTO collaborator,
                                                       EquipmentSummaryDTO equipment) {
        return EquipmentCollaboratorResponseDTO.builder()
                .id(entity.getId())
                .deliveryDate(entity.getDeliveryDate())
                .previsionDeliveryDate(entity.getPrevisionDeliveryDate())
                .returnDate(entity.getReturnDate())
                .deliveryStatus(entity.getDeliveryStatus())
                .notes(entity.getNotes())
                .collaborator(collaborator)
                .equipment(equipment)
                .build();
    }

    public EquipmentCollaboratorResponseDTO mapToResponseDTO(EquipmentCollaboratorEntity entity) {
        var collaboratorResponse = mapperCollaborator.toSummaryResponse(entity.getCollaborator());
        var equipmentResponse = mapperEquipment.toSummaryResponse(entity.getEquipment());
        return toResponse(entity, collaboratorResponse, equipmentResponse);
    }
}
package br.com.raroacademy.demo.domain.DTO.expectedReturn;

import br.com.raroacademy.demo.domain.DTO.equipment.collaborator.EquipmentCollaboratorResponseDTO;
import br.com.raroacademy.demo.domain.DTO.equipment.collaborator.MapperEquipmentCollaborator;
import br.com.raroacademy.demo.domain.entities.EquipmentCollaboratorEntity;
import br.com.raroacademy.demo.domain.entities.ExpectedReturnEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class MapperExpectedReturn {

    private final MapperEquipmentCollaborator mapperEquipmentCollaborator;

    public ExpectedReturnEntity toEntity(br.com.raroacademy.demo.domain.DTO.expectedReturn.ExpectedReturnRequestDTO dto, EquipmentCollaboratorEntity equipmentCollaborator) {
        return ExpectedReturnEntity.builder()
                .expectedReturnDate(dto.expectedReturnDate())
                .equipmentCollaborator(equipmentCollaborator)
                .build();
    }

    public br.com.raroacademy.demo.domain.DTO.expectedReturn.ExpectedReturnResponseDTO toResponse(ExpectedReturnEntity entity, EquipmentCollaboratorResponseDTO equipmentCollaboratorResponse) {
        return br.com.raroacademy.demo.domain.DTO.expectedReturn.ExpectedReturnResponseDTO.builder()
                .id(entity.getId())
                .expectedReturnDate(entity.getExpectedReturnDate())
                .equipmentCollaborator(equipmentCollaboratorResponse)
                .build();
    }
    public br.com.raroacademy.demo.domain.DTO.expectedReturn.ExpectedReturnResponseDTO mapToResponseDTO(ExpectedReturnEntity entity) {
        var equipmentCollaboratorResponse = mapperEquipmentCollaborator.mapToResponseDTO(entity.getEquipmentCollaborator());
        return toResponse(entity, equipmentCollaboratorResponse);
    }
}
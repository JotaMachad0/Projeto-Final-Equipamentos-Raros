package br.com.raroacademy.demo.domain.DTO.expectedReturn;

import br.com.raroacademy.demo.domain.DTO.equipmentCollaborator.EquipmentCollaboratorResponseDTO;
import br.com.raroacademy.demo.domain.DTO.equipmentCollaborator.MapperEquipmentCollaborator;
import br.com.raroacademy.demo.domain.entities.EquipmentCollaboratorEntity;
import br.com.raroacademy.demo.domain.entities.ExpectedReturnEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class MapperExpectedReturn {

    private final MapperEquipmentCollaborator mapperEquipmentCollaborator;

    public ExpectedReturnEntity toEntity(ExpectedReturnRequestDTO dto, EquipmentCollaboratorEntity equipmentCollaborator) {
        return ExpectedReturnEntity.builder()
                .expectedReturnDate(dto.getExpectedReturnDate())
                .equipmentCollaborator(equipmentCollaborator)
                .build();
    }

    public ExpectedReturnResponseDTO toResponse(ExpectedReturnEntity entity, EquipmentCollaboratorResponseDTO equipmentCollaboratorResponse) {
        return ExpectedReturnResponseDTO.builder()
                .id(entity.getId())
                .expectedReturnDate(entity.getExpectedReturnDate())
                .equipmentCollaborator(equipmentCollaboratorResponse)
                .build();
    }
    public ExpectedReturnResponseDTO mapToResponseDTO(ExpectedReturnEntity entity) {
        var equipmentCollaboratorResponse = mapperEquipmentCollaborator.mapToResponseDTO(entity.getEquipmentCollaborator());
        return toResponse(entity, equipmentCollaboratorResponse);
    }
}
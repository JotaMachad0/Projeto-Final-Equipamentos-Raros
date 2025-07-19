package br.com.raroacademy.demo.domain.DTO.equipment;

import br.com.raroacademy.demo.domain.entities.EquipmentEntity;
import br.com.raroacademy.demo.domain.enums.EquipmentType;
import org.springframework.stereotype.Component;

@Component
public class MapperEquipment {

    public EquipmentEntity toEntity(EquipmentRequestDTO dto) {
        return EquipmentEntity.builder()
                .type(EquipmentType.valueOf(dto.type().toUpperCase()))
                .serialNumber(dto.serialNumber())
                .brand(dto.brand())
                .model(dto.model())
                .specs(dto.specs())
                .acquisitionDate(dto.acquisitionDate())
                .usageTimeMonths(dto.usageTimeMonths())
                .status(dto.status())
                .build();
    }

    public EquipmentResponseDTO toDTO(EquipmentEntity entity) {
        return EquipmentResponseDTO.builder()
                .id(entity.getId())
                .type(entity.getType().name())
                .serialNumber(entity.getSerialNumber())
                .brand(entity.getBrand())
                .model(entity.getModel())
                .specs(entity.getSpecs())
                .acquisitionDate(entity.getAcquisitionDate())
                .usageTimeMonths(entity.getUsageTimeMonths())
                .status(entity.getStatus())
                .build();
    }

    public EquipmentEntity toUpdateEquipment(EquipmentEntity entity, EquipmentRequestDTO request) {
        entity.setType(EquipmentType.valueOf(request.type().toUpperCase()));
        entity.setSerialNumber(request.serialNumber());
        entity.setBrand(request.brand());
        entity.setModel(request.model());
        entity.setSpecs(request.specs());
        entity.setAcquisitionDate(request.acquisitionDate());
        entity.setUsageTimeMonths(request.usageTimeMonths());
        entity.setStatus(request.status());
        return entity;
    }

    public EquipmentSummaryDTO toSummaryResponse(EquipmentEntity entity) {
        return EquipmentSummaryDTO.builder()
                .id(entity.getId())
                .type(entity.getType().toString())
                .model(entity.getModel())
                .build();
    }
}

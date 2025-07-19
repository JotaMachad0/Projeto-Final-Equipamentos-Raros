package br.com.raroacademy.demo.domain.DTO.equipment;

import br.com.raroacademy.demo.domain.enums.EquipmentStatus;
import br.com.raroacademy.demo.domain.enums.EquipmentType;
import br.com.raroacademy.demo.domain.entities.EquipmentEntity;
import org.springframework.stereotype.Component;

@Component
public class MapperEquipment {

    public EquipmentEntity toEntity(EquipmentRequestDTO dto) {
        return EquipmentEntity.builder()
                .type(EquipmentType.valueOf(dto.getType().toUpperCase()))
                .serialNumber(dto.getSerialNumber())
                .brand(dto.getBrand())
                .model(dto.getModel())
                .specs(dto.getSpecs())
                .acquisitionDate(dto.getAcquisitionDate())
                .usageTimeMonths(dto.getUsageTimeMonths())
                .status(EquipmentStatus.valueOf(dto.getStatus()))
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
                .status(String.valueOf(entity.getStatus()))
                .build();
    }

    public EquipmentEntity toUpdateEquipment(EquipmentEntity entity, EquipmentRequestDTO request) {
        entity.setType(EquipmentType.valueOf(request.getType().toUpperCase()));
        entity.setSerialNumber(request.getSerialNumber());
        entity.setBrand(request.getBrand());
        entity.setModel(request.getModel());
        entity.setSpecs(request.getSpecs());
        entity.setAcquisitionDate(request.getAcquisitionDate());
        entity.setUsageTimeMonths(request.getUsageTimeMonths());
        entity.setStatus(EquipmentStatus.valueOf(request.getStatus()));
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

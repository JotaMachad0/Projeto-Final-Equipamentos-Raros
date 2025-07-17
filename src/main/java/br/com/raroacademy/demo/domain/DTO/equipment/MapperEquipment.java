package br.com.raroacademy.demo.domain.DTO.equipment;

import br.com.raroacademy.demo.domain.entities.EquipmentEntity;
import org.springframework.stereotype.Component;

@Component
public class MapperEquipment {

    public EquipmentEntity toEntity(EquipmentRequestDTO dto) {
        return EquipmentEntity.builder()
                .type(dto.getType())
                .serialNumber(dto.getSerialNumber())
                .brand(dto.getBrand())
                .model(dto.getModel())
                .specs(dto.getSpecs())
                .acquisitionDate(dto.getAcquisitionDate())
                .usageTimeMonths(dto.getUsageTimeMonths())
                .status(dto.getStatus())
                .build();
    }

    public EquipmentResponseDTO toDTO(EquipmentEntity entity) {
        return EquipmentResponseDTO.builder()
                .id(entity.getId())
                .type(entity.getType())
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
        entity.setType(request.getType());
        entity.setSerialNumber(request.getSerialNumber());
        entity.setBrand(request.getBrand());
        entity.setModel(request.getModel());
        entity.setSpecs(request.getSpecs());
        entity.setAcquisitionDate(request.getAcquisitionDate());
        entity.setUsageTimeMonths(request.getUsageTimeMonths());
        entity.setStatus(request.getStatus());
        return entity;
    }
}

package br.com.raroacademy.demo.domain.DTO.equipment;

import br.com.raroacademy.demo.domain.entities.EquipmentEntity;

public class MapperEquipment {

    public static EquipmentEntity toEntity(EquipmentRequestDTO dto) {
        return EquipmentEntity.builder()
                .id(dto.getId())
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

    public static EquipmentResponseDTO toDTO(EquipmentEntity entity) {
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
}

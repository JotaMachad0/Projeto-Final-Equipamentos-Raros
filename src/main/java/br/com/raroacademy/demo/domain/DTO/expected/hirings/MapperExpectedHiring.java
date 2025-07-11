package br.com.raroacademy.demo.domain.DTO.expected.hirings;

import br.com.raroacademy.demo.domain.entities.ExpectedHiringEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MapperExpectedHiring {
    public ExpectedHiringEntity toExpectedHiring(ExpectedHiringRequestDTO request) {
        return ExpectedHiringEntity.builder()
                .expectedHireDate(request.expectedHireDate())
                .position(request.position())
                .equipmentRequirements(request.equipmentRequirements())
                .region(request.region())
                .build();
    }

    public ExpectedHiringResponseDTO toExpectedHiringResponseDTO(ExpectedHiringEntity entity) {
        return ExpectedHiringResponseDTO.builder()
                .id(entity.getId())
                .expectedHireDate(entity.getExpectedHireDate())
                .position(entity.getPosition())
                .equipmentRequirements(entity.getEquipmentRequirements())
                .region(entity.getRegion())
                .build();
    }

    public ExpectedHiringEntity toApplyUpdates(ExpectedHiringEntity existing, ExpectedHiringRequestDTO request) {
        return ExpectedHiringEntity.builder()
                .id(existing.getId())
                .expectedHireDate(request.expectedHireDate())
                .position(request.position())
                .equipmentRequirements(request.equipmentRequirements())
                .region(request.region())
                .build();
    }

    public List<ExpectedHiringResponseDTO> toExpectedHiringList(List<ExpectedHiringEntity> entities) {
        return entities.stream()
                .map(this::toExpectedHiringResponseDTO)
                .toList();
    }
}

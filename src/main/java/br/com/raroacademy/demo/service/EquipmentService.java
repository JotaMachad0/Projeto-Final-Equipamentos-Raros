package br.com.raroacademy.demo.service;

import br.com.raroacademy.demo.domain.DTO.equipment.EquipmentRequestDTO;
import br.com.raroacademy.demo.domain.DTO.equipment.EquipmentResponseDTO;
import br.com.raroacademy.demo.domain.DTO.equipment.MapperEquipment;
import br.com.raroacademy.demo.domain.entities.EquipmentEntity;
import br.com.raroacademy.demo.repository.EquipmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EquipmentService {

    private final EquipmentRepository repository;

    public EquipmentService(EquipmentRepository repository) {
        this.repository = repository;
    }

    public EquipmentResponseDTO create(EquipmentRequestDTO dto) {
        EquipmentEntity entity = MapperEquipment.toEntity(dto);
        repository.save(entity);
        return MapperEquipment.toDTO(entity);
    }

    public List<EquipmentResponseDTO> getAll() {
        return repository.findAll()
                .stream()
                .map(MapperEquipment::toDTO)
                .collect(Collectors.toList());
    }
}

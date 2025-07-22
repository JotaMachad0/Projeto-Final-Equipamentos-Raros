package br.com.raroacademy.demo.service;

import br.com.raroacademy.demo.commons.i18n.I18nUtil;
import br.com.raroacademy.demo.domain.DTO.equipment.EquipmentRequestDTO;
import br.com.raroacademy.demo.domain.DTO.equipment.EquipmentResponseDTO;
import br.com.raroacademy.demo.domain.DTO.equipment.MapperEquipment;
import br.com.raroacademy.demo.domain.entities.EquipmentEntity;
import br.com.raroacademy.demo.domain.enums.EquipmentStatus;
import br.com.raroacademy.demo.exception.NotFoundException;
import br.com.raroacademy.demo.repository.EquipmentRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EquipmentService {

    private final EquipmentRepository equipmentRepository;
    private final MapperEquipment mapperEquipment;
    private final StockService stockService;
    private final I18nUtil i18n;

    @Transactional
    public EquipmentResponseDTO create(EquipmentRequestDTO dto) {
        EquipmentEntity entity = mapperEquipment.toEntity(dto);
        equipmentRepository.save(entity);
        stockService.incrementStock(entity.getType());
        return mapperEquipment.toDTO(entity);
    }

    public EquipmentResponseDTO getEquipment(Long id) {
        var equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(i18n.getMessage("equipment.not.found")));
        return mapperEquipment.toDTO(equipment);
    }

    @Transactional
    public EquipmentResponseDTO update(Long id, @Valid EquipmentRequestDTO request) {
        var existingEquipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(i18n.getMessage("equipment.not.found")));

        if (!existingEquipment.getSerialNumber().equals(request.serialNumber()) &&
                equipmentRepository.existsBySerialNumber(request.serialNumber())) {
            throw new DataIntegrityViolationException(i18n.getMessage("equipment.serialNumber.already.exists"));
        }

        var updatedEquipment = mapperEquipment.toUpdateEquipment(existingEquipment, request);
        var savedEquipment = equipmentRepository.save(updatedEquipment);

        return mapperEquipment.toDTO(savedEquipment);
    }

    @Transactional
    public void delete(Long id) {
        var equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(i18n.getMessage("equipment.not.found")));
        equipmentRepository.delete(equipment);
        stockService.decrementStock(equipment.getType());
    }

    public List<EquipmentResponseDTO> getAll() {
        return equipmentRepository.findAll()
                .stream()
                .map(mapperEquipment::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateEquipmentStatus(Long equipmentId, EquipmentStatus newStatus) {
        EquipmentEntity equipment = equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new NotFoundException(i18n.getMessage("equipment.not.found")));
        equipment.setStatus(newStatus);
        equipmentRepository.save(equipment);
    }
}

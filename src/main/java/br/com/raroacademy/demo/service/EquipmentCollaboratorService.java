package br.com.raroacademy.demo.service;

import br.com.raroacademy.demo.domain.DTO.collaborator.MapperCollaborator;
import br.com.raroacademy.demo.domain.DTO.equipment.MapperEquipment;
import br.com.raroacademy.demo.domain.DTO.equipmentCollaborator.EquipmentCollaboratorRequestDTO;
import br.com.raroacademy.demo.domain.DTO.equipmentCollaborator.EquipmentCollaboratorResponseDTO;
import br.com.raroacademy.demo.domain.DTO.equipmentCollaborator.MapperEquipmentCollaborator;
import br.com.raroacademy.demo.domain.entities.CollaboratorEntity;
import br.com.raroacademy.demo.domain.entities.EquipmentCollaboratorEntity;
import br.com.raroacademy.demo.domain.entities.EquipmentEntity;
import br.com.raroacademy.demo.exception.DataIntegrityViolationException;
import br.com.raroacademy.demo.exception.NotFoundException;
import br.com.raroacademy.demo.repository.CollaboratorRepository;
import br.com.raroacademy.demo.repository.EquipmentCollaboratorRepository;
import br.com.raroacademy.demo.repository.EquipmentRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EquipmentCollaboratorService {

    private final EquipmentCollaboratorRepository equipmentCollaboratorRepository;
    private final CollaboratorRepository collaboratorRepository;
    private final EquipmentRepository equipmentRepository;

    private final MapperEquipmentCollaborator mapperEquipmentCollaborator;
    private final MapperCollaborator mapperCollaborator;
    private final MapperEquipment mapperEquipment;

    private final MessageSource messageSource;

    private String getMessage(String code) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(code, null, locale);
    }

    public EquipmentCollaboratorResponseDTO create(EquipmentCollaboratorRequestDTO request) {

        CollaboratorEntity collaborator = collaboratorRepository.findById(request.getCollaboratorId())
                .orElseThrow(() -> new NotFoundException(getMessage("collaborator.not-found")));

        EquipmentEntity equipment = equipmentRepository.findById(request.getEquipmentId())
                .orElseThrow(() -> new NotFoundException(getMessage("equipment.not-found")));

        var unavailableEquipments = equipmentCollaboratorRepository
                .findUnavailableEquipments(request.getEquipmentId(), request.getDeliveryDate());

        if (!unavailableEquipments.isEmpty()) {
            var conflictingAssignment = unavailableEquipments.getFirst();
            if (conflictingAssignment.getReturnDate() == null) {
                throw new DataIntegrityViolationException(getMessage("equipment.unavailable.indefinitely"));
            } else {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                String formattedReturnDate = conflictingAssignment.getReturnDate().format(formatter);
                String message = messageSource.getMessage("equipment.unavailable.until", new Object[]{formattedReturnDate}, LocaleContextHolder.getLocale());
                throw new DataIntegrityViolationException(message);
            }
        }

        var entityToSave = mapperEquipmentCollaborator.toEntity(request, collaborator, equipment);
        var savedEntity = equipmentCollaboratorRepository.save(entityToSave);

        var collaboratorResponse = mapperCollaborator.toSummaryResponse(savedEntity.getCollaborator());
        var equipmentResponse = mapperEquipment.toSummaryResponse(savedEntity.getEquipment());

        return mapperEquipmentCollaborator.toResponse(savedEntity, collaboratorResponse, equipmentResponse);
    }

    public EquipmentCollaboratorResponseDTO getById(Long id) {
        EquipmentCollaboratorEntity entity = equipmentCollaboratorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(getMessage("equipment-collaborator.not-found")));

        var collaboratorResponse = mapperCollaborator.toSummaryResponse(entity.getCollaborator());
        var equipmentResponse = mapperEquipment.toSummaryResponse(entity.getEquipment());

        return mapperEquipmentCollaborator.toResponse(entity, collaboratorResponse, equipmentResponse);
    }

    public List<EquipmentCollaboratorResponseDTO> getAll() {
        return equipmentCollaboratorRepository.findAll().stream()
                .map(mapperEquipmentCollaborator::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public EquipmentCollaboratorResponseDTO update(Long id, EquipmentCollaboratorRequestDTO request) {
        EquipmentCollaboratorEntity existingEntity = equipmentCollaboratorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(getMessage("equipment-collaborator.not-found")));

        CollaboratorEntity collaborator = collaboratorRepository.findById(request.getCollaboratorId())
                .orElseThrow(() -> new NotFoundException(getMessage("collaborator.not-found")));

        EquipmentEntity equipment = equipmentRepository.findById(request.getEquipmentId())
                .orElseThrow(() -> new NotFoundException(getMessage("equipment.not-found")));

        existingEntity.setCollaborator(collaborator);
        existingEntity.setEquipment(equipment);
        existingEntity.setDeliveryDate(request.getDeliveryDate());
        existingEntity.setReturnDate(request.getReturnDate());
        existingEntity.setDeliveryStatus(request.getDeliveryStatus());
        existingEntity.setNotes(request.getNotes());

        var updatedEntity = equipmentCollaboratorRepository.save(existingEntity);
        return mapperEquipmentCollaborator.mapToResponseDTO(updatedEntity);
    }

    public void delete(Long id) {
        if (!equipmentCollaboratorRepository.existsById(id)) {
            throw new NotFoundException(getMessage("equipment-collaborator.not-found"));
        }
        equipmentCollaboratorRepository.deleteById(id);
    }

}
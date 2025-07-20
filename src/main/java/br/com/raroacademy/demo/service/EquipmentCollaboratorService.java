package br.com.raroacademy.demo.service;

import br.com.raroacademy.demo.domain.DTO.collaborator.MapperCollaborator;
import br.com.raroacademy.demo.domain.DTO.equipment.MapperEquipment;
import br.com.raroacademy.demo.domain.DTO.equipment.collaborator.EquipmentCollaboratorRequestDTO;
import br.com.raroacademy.demo.domain.DTO.equipment.collaborator.EquipmentCollaboratorResponseDTO;
import br.com.raroacademy.demo.domain.DTO.equipment.collaborator.MapperEquipmentCollaborator;

import br.com.raroacademy.demo.domain.enums.EquipmentStatus;
import br.com.raroacademy.demo.exception.DataIntegrityViolationException;
import br.com.raroacademy.demo.exception.NotFoundException;
import br.com.raroacademy.demo.repository.AddressRepository;
import br.com.raroacademy.demo.repository.CollaboratorRepository;
import br.com.raroacademy.demo.repository.EquipmentCollaboratorRepository;
import br.com.raroacademy.demo.repository.EquipmentRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EquipmentCollaboratorService {

    private final EquipmentCollaboratorRepository equipmentCollaboratorRepository;
    private final CollaboratorRepository collaboratorRepository;
    private final EquipmentRepository equipmentRepository;
    private final AddressRepository addressRepository;

    private final StockService stockService;

    private final MapperEquipmentCollaborator mapperEquipmentCollaborator;
    private final MapperCollaborator mapperCollaborator;
    private final MapperEquipment mapperEquipment;

    private final DeliveryTimeService deliveryTimeService;

    private final MessageSource messageSource;

    private String getMessage(String code) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(code, null, locale);
    }

    @Transactional
    public EquipmentCollaboratorResponseDTO create(EquipmentCollaboratorRequestDTO request) {

        var collaborator = collaboratorRepository.findById(request.getCollaboratorId())
                .orElseThrow(() -> new NotFoundException(getMessage("collaborator.not-found")));

        var equipment = equipmentRepository.findById(request.getEquipmentId())
                .orElseThrow(() -> new NotFoundException(getMessage("equipment.not-found")));


        if (equipment.getStatus() != EquipmentStatus.AVAILABLE) {
            throw new DataIntegrityViolationException(getMessage("equipment.unavailable.status")
            );
        }

        stockService.decrementStock(equipment.getType());

        equipment.setStatus(EquipmentStatus.IN_USE);
        equipmentRepository.save(equipment);
        var address = addressRepository.findById(collaborator.getAddressId())
                .orElseThrow(() -> new NotFoundException(getMessage("address.not-found")));

        var entityToSave = mapperEquipmentCollaborator.toEntity(request, collaborator, equipment);

        var previsionDate = deliveryTimeService.calculate(address.getState(), entityToSave.getDeliveryDate());
        entityToSave.setPrevisionDeliveryDate(previsionDate);

        var savedEntity = equipmentCollaboratorRepository.save(entityToSave);

        var collaboratorResponse = mapperCollaborator.toSummaryResponse(savedEntity.getCollaborator());
        var equipmentResponse = mapperEquipment.toSummaryResponse(savedEntity.getEquipment());

        return mapperEquipmentCollaborator.toResponse(savedEntity, collaboratorResponse, equipmentResponse);
    }

    public EquipmentCollaboratorResponseDTO getById(Long id) {
        var entity = equipmentCollaboratorRepository.findById(id)
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

    @Transactional
    public EquipmentCollaboratorResponseDTO update(Long id, EquipmentCollaboratorRequestDTO request) {
        var existingEntity = equipmentCollaboratorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(getMessage("equipment-collaborator.not-found")));

        boolean isReturning = existingEntity.getReturnDate() == null && request.getReturnDate() != null;

        if (isReturning) {
            LocalDate returnDate = request.getReturnDate();

            if (returnDate.isAfter(LocalDate.now())) {
                throw new DataIntegrityViolationException(getMessage("return.date.future"));
            }

            var equipmentToReturn = existingEntity.getEquipment();

            stockService.incrementStock(equipmentToReturn.getType());

            equipmentToReturn.setStatus(EquipmentStatus.AVAILABLE);
            equipmentRepository.save(equipmentToReturn);
        }

        var collaborator = collaboratorRepository.findById(request.getCollaboratorId())
                .orElseThrow(() -> new NotFoundException(getMessage("collaborator.not-found")));
        var equipment = equipmentRepository.findById(request.getEquipmentId())
                .orElseThrow(() -> new NotFoundException(getMessage("equipment.not-found")));

        existingEntity.setCollaborator(collaborator);
        if (!isReturning) {
            existingEntity.setEquipment(equipment);
        }
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
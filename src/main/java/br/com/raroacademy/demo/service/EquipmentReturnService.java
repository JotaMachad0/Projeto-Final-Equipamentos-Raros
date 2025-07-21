package br.com.raroacademy.demo.service;

import br.com.raroacademy.demo.domain.DTO.equipment.returns.CreateReturnScheduleRequestDTO;
import br.com.raroacademy.demo.domain.DTO.equipment.returns.ProcessReturnRequestDTO;
import br.com.raroacademy.demo.domain.DTO.equipment.returns.EquipmentReturnResponseDTO;
import br.com.raroacademy.demo.domain.DTO.equipment.returns.MapperEquipmentReturn;
import br.com.raroacademy.demo.domain.entities.EquipmentReturnEntity;
import br.com.raroacademy.demo.domain.enums.EquipmentStatus;
import br.com.raroacademy.demo.domain.enums.ReturnStatus;
import br.com.raroacademy.demo.exception.BusinessException;
import br.com.raroacademy.demo.exception.NotFoundException;
import br.com.raroacademy.demo.repository.EquipmentCollaboratorRepository;
import br.com.raroacademy.demo.repository.EquipmentReturnRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EquipmentReturnService {

    private final EquipmentReturnRepository equipmentReturnRepository;
    private final EquipmentCollaboratorRepository equipmentCollaboratorRepository;
    private final MapperEquipmentReturn mapperEquipmentReturn;
    private final MessageSource messageSource;

    private final EquipmentService equipmentService;
    private final EquipmentCollaboratorService equipmentCollaboratorService;
    private final StockService stockService;

    private String getMessage(String code) {
        return messageSource.getMessage(code, null, LocaleContextHolder.getLocale());
    }

    @Transactional
    public EquipmentReturnResponseDTO createSchedule(CreateReturnScheduleRequestDTO request) {
        var equipmentCollaborator = equipmentCollaboratorRepository
                .findById(request.equipmentCollaboratorId())
                .orElseThrow(() -> new NotFoundException(getMessage("equipment-collaborator.not-found")));

        boolean exists = equipmentReturnRepository.existsByEquipmentCollaboratorIdAndStatus(
                request.equipmentCollaboratorId(), ReturnStatus.PENDING
        );
        if (exists) {
            throw new BusinessException(getMessage("equipment-return.pending.already-exists"));
        }

        EquipmentReturnEntity newReturn = EquipmentReturnEntity.builder()
                .deliveryDate(request.deliveryDate())
                .equipmentCollaborator(equipmentCollaborator)
                .status(ReturnStatus.PENDING)
                .build();

        var savedEntity = equipmentReturnRepository.save(newReturn);
        return mapperEquipmentReturn.mapToResponseDTO(savedEntity);
    }

    @Transactional
    public EquipmentReturnResponseDTO processReturn(Long returnId, ProcessReturnRequestDTO request) {
        EquipmentReturnEntity returnEntity = equipmentReturnRepository.findById(returnId)
                .orElseThrow(() -> new NotFoundException(getMessage("equipment-return.not-found")));

        if (returnEntity.getStatus() != ReturnStatus.PENDING) {
            throw new BusinessException(getMessage("equipment-return.not-pending"));
        }

        returnEntity.setReceiptDate(request.receiptDate());
        returnEntity.setNote(request.note());
        returnEntity.setStatus(ReturnStatus.RETURNED);

        var collaboratorLink = returnEntity.getEquipmentCollaborator();
        var equipment = collaboratorLink.getEquipment();

        if (request.equipmentCurrentStatus() == EquipmentStatus.DEFECTIVE) {
            equipmentService.updateEquipmentStatus(equipment.getId(), EquipmentStatus.DEFECTIVE);
            stockService.calculateStock(equipment.getType());
        } else {
            equipmentService.updateEquipmentStatus(equipment.getId(), EquipmentStatus.AVAILABLE);
            stockService.incrementStock(equipment.getType());
        }
        equipmentCollaboratorService.finalizeLoan(collaboratorLink.getId());

        var savedEntity = equipmentReturnRepository.save(returnEntity);
        return mapperEquipmentReturn.mapToResponseDTO(savedEntity);
    }

    public List<EquipmentReturnResponseDTO> getAll() {
        return equipmentReturnRepository.findAll()
                .stream()
                .map(mapperEquipmentReturn::mapToResponseDTO)
                .collect(Collectors.toList());
    }
}
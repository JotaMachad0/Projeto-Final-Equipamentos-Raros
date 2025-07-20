package br.com.raroacademy.demo.service;

import br.com.raroacademy.demo.domain.DTO.expected.returns.ExpectedReturnRequestDTO;
import br.com.raroacademy.demo.domain.DTO.expected.returns.ExpectedReturnResponseDTO;
import br.com.raroacademy.demo.domain.DTO.expected.returns.MapperExpectedReturn;
import br.com.raroacademy.demo.domain.entities.EquipmentCollaboratorEntity;
import br.com.raroacademy.demo.domain.entities.ExpectedReturnEntity;
import br.com.raroacademy.demo.exception.DataIntegrityViolationException;
import br.com.raroacademy.demo.exception.NotFoundException;
import br.com.raroacademy.demo.repository.EquipmentCollaboratorRepository;
import br.com.raroacademy.demo.repository.ExpectedReturnRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ExpectedReturnService {

    private final ExpectedReturnRepository expectedReturnRepository;
    private final EquipmentCollaboratorRepository equipmentCollaboratorRepository;
    private final MapperExpectedReturn mapperExpectedReturn;
    private final MessageSource messageSource;

    private String getMessage(String code) {
        return messageSource.getMessage(code, null, LocaleContextHolder.getLocale());
    }

    public ExpectedReturnResponseDTO create(ExpectedReturnRequestDTO request) {
        EquipmentCollaboratorEntity equipmentCollaborator = equipmentCollaboratorRepository
                .findById(request.equipmentCollaboratorId())
                .orElseThrow(() -> new NotFoundException(getMessage("equipment-collaborator.not-found")));

        if (expectedReturnRepository.existsByEquipmentCollaboratorId(request.equipmentCollaboratorId())) {
            throw new DataIntegrityViolationException(getMessage("expected-return.already-exists"));
        }

        var entityToSave = mapperExpectedReturn.toEntity(request, equipmentCollaborator);
        var savedEntity = expectedReturnRepository.save(entityToSave);

        return mapperExpectedReturn.mapToResponseDTO(savedEntity);
    }

    public ExpectedReturnResponseDTO getById(Long id) {
        ExpectedReturnEntity entity = expectedReturnRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(getMessage("expected-return.not-found")));
        return mapperExpectedReturn.mapToResponseDTO(entity);
    }

    public List<ExpectedReturnResponseDTO> getAll() {
        return expectedReturnRepository.findAll().stream()
                .map(mapperExpectedReturn::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ExpectedReturnResponseDTO update(Long id, ExpectedReturnRequestDTO request) {
        ExpectedReturnEntity existingEntity = expectedReturnRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(getMessage("expected-return.not-found")));

        EquipmentCollaboratorEntity equipmentCollaborator = equipmentCollaboratorRepository
                .findById(request.equipmentCollaboratorId())
                .orElseThrow(() -> new NotFoundException(getMessage("equipment-collaborator.not-found")));

        if (!existingEntity.getEquipmentCollaborator().getId().equals(request.equipmentCollaboratorId()) &&
                expectedReturnRepository.existsByEquipmentCollaboratorId(request.equipmentCollaboratorId())) {
            throw new DataIntegrityViolationException(getMessage("expected-return.already-exists"));
        }

        existingEntity.setExpectedReturnDate(request.expectedReturnDate());
        existingEntity.setEquipmentCollaborator(equipmentCollaborator);

        var updatedEntity = expectedReturnRepository.save(existingEntity);
        return mapperExpectedReturn.mapToResponseDTO(updatedEntity);
    }

    public void delete(Long id) {
        if (!expectedReturnRepository.existsById(id)) {
            throw new NotFoundException(getMessage("expected-return.not-found"));
        }
        expectedReturnRepository.deleteById(id);
    }

}
package br.com.raroacademy.demo.service;

import br.com.raroacademy.demo.commons.i18n.I18nUtil;
import br.com.raroacademy.demo.domain.DTO.address.MapperAddress;
import br.com.raroacademy.demo.domain.DTO.collaborator.*;
import br.com.raroacademy.demo.domain.DTO.viaCep.ViaCepResponseDTO;
import br.com.raroacademy.demo.domain.entities.AddressEntity;
import br.com.raroacademy.demo.domain.entities.CollaboratorEntity;
import br.com.raroacademy.demo.domain.entities.EquipmentCollaboratorEntity;
import br.com.raroacademy.demo.exception.InvalidCepException;
import br.com.raroacademy.demo.exception.NotFoundException;
import br.com.raroacademy.demo.exception.DataIntegrityViolationException;
import br.com.raroacademy.demo.repository.AddressRepository;
import br.com.raroacademy.demo.repository.CollaboratorRepository;
import br.com.raroacademy.demo.client.ViaCepClient;
import br.com.raroacademy.demo.repository.EquipmentCollaboratorRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CollaboratorService {

    private final CollaboratorRepository collaboratorRepository;
    private final MapperCollaborator mapperCollaborator;
    private final MapperAddress mapperAddress;
    private final AddressRepository addressRepository;
    private final ViaCepClient viaCepClient;
    private final ViaCepService viaCepService;
    private final MessageSource messageSource;
    private final EquipmentCollaboratorRepository equipmentCollaboratorRepository;
    private final I18nUtil i18n;

    private String getMessage(String code) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(code, null, locale);
    }

    public CollaboratorResponseDTO save(CollaboratorRequestDTO request) {

        CompletableFuture<ViaCepResponseDTO> viaCepFuture = viaCepService.buscarEnderecoPorCepAsync(request.cep());

        if (collaboratorRepository.existsByCpf(request.cpf())) {
            throw new DataIntegrityViolationException(i18n.getMessage("collaborator.cpf.already-exists"));
        }
        if (collaboratorRepository.existsByEmail(request.email())) {
            throw new DataIntegrityViolationException(i18n.getMessage("collaborator.email.already-exists"));
        }

        ViaCepResponseDTO viaCep;
        try {
            viaCep = viaCepFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            throw new InvalidCepException("cep.service.error");
        }

        if (viaCep.getCep() == null) {
            throw new InvalidCepException(getMessage("address.cep.invalid"));
        }

        var addressEntity = mapperAddress.toEntity(request, viaCep);
        var addressSaved = addressRepository.save(addressEntity);

        var entity = mapperCollaborator.toEntity(request, addressSaved.getId());
        var saved = collaboratorRepository.save(entity);
        var address = mapperAddress.toDTO(addressSaved);
        return mapperCollaborator.toResponse(saved, address);
    }

    public CollaboratorResponseDTO getById(Long id) {
        CollaboratorEntity collaboratorEntity = collaboratorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(getMessage("collaborator.not-found")));

        AddressEntity addressEntity = addressRepository.findById(collaboratorEntity.getAddressId())
                .orElseThrow(() -> new NotFoundException(getMessage("address.not-found")));

        var address = mapperAddress.toDTO(addressEntity);
        return mapperCollaborator.toResponse(collaboratorEntity, address);
    }

    public List<CollaboratorResponseDTO> getAll() {
        return collaboratorRepository.findAll().stream()
                .map(collaborator -> {
                    AddressEntity address = addressRepository.findById(collaborator.getAddressId())
                            .orElseThrow(() -> new NotFoundException(getMessage("address.not-found")));
                    return mapperCollaborator.toResponse(collaborator, mapperAddress.toDTO(address));
                })
                .collect(Collectors.toList());
    }

    public CollaboratorResponseDTO update(Long id, CollaboratorRequestDTO dto) {
        CollaboratorEntity existing = collaboratorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(getMessage("collaborator.not-found")));

        AddressEntity address = addressRepository.findById(existing.getAddressId())
                .orElseThrow(() -> new NotFoundException(getMessage("address.not-found")));

        var viaCep = viaCepClient.buscarEnderecoPorCep(dto.cep());
        if (viaCep.getCep() == null) {
            throw new InvalidCepException(getMessage("address.cep.invalid"));
        }

        address.setCep(viaCep.getCep());
        address.setStreet(viaCep.getStreet());
        address.setNeighborhood(viaCep.getNeighborhood());
        address.setCity(viaCep.getCity());
        address.setState(viaCep.getState());
        address.setNumber(dto.number());
        address.setComplement(dto.complement());

        addressRepository.save(address);

        existing.setName(dto.name());
        existing.setEmail(dto.email());
        existing.setPhone(dto.phone());
        existing.setContractStartDate(dto.contractStartDate());
        existing.setContractEndDate(dto.contractEndDate());

        CollaboratorEntity updated = collaboratorRepository.save(existing);
        return mapperCollaborator.toResponse(updated, mapperAddress.toDTO(address));
    }

    public void delete(Long id) {
        if (!collaboratorRepository.existsById(id)) {
            throw new NotFoundException(getMessage("collaborator.not-found"));
        }
        collaboratorRepository.deleteById(id);
    }

    public DismissalResponseDTO dismiss(Long id, DismissalRequestDTO dto) {
        CollaboratorEntity collaborator = collaboratorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(i18n.getMessage("collaborator.not-found")));

        collaborator.setContractEndDate(dto.dismissalDate());
        CollaboratorEntity updatedCollaborator = collaboratorRepository.save(collaborator);

        List<EquipmentCollaboratorEntity> activeLoansEntities =
                equipmentCollaboratorRepository.findByCollaboratorIdAndReturnDateIsNull(id);

        List<DismissalResponseDTO.ActiveLoanDTO> activeLoansDTO = activeLoansEntities.stream()
                .map(loan -> new DismissalResponseDTO.ActiveLoanDTO(
                        loan.getId(),
                        loan.getEquipment().getId(),
                        loan.getEquipment().getType().toString(),
                        loan.getDeliveryDate()
                )).collect(Collectors.toList());

        return new DismissalResponseDTO(
                updatedCollaborator.getId(),
                updatedCollaborator.getName(),
                updatedCollaborator.getEmail(),
                updatedCollaborator.getContractEndDate(),
                activeLoansDTO
        );
    }

}

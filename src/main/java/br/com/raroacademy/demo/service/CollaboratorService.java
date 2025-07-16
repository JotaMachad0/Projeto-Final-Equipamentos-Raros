package br.com.raroacademy.demo.service;

import br.com.raroacademy.demo.domain.DTO.address.MapperAddress;
import br.com.raroacademy.demo.domain.DTO.collaborator.CollaboratorRequestDTO;
import br.com.raroacademy.demo.domain.DTO.collaborator.CollaboratorResponseDTO;
import br.com.raroacademy.demo.domain.DTO.collaborator.MapperCollaborator;
import br.com.raroacademy.demo.domain.entities.AddressEntity;
import br.com.raroacademy.demo.domain.entities.CollaboratorEntity;
import br.com.raroacademy.demo.exception.InvalidCepException;
import br.com.raroacademy.demo.exception.NotFoundException;
import br.com.raroacademy.demo.exception.DataIntegrityViolationException;
import br.com.raroacademy.demo.repository.AddressRepository;
import br.com.raroacademy.demo.repository.CollaboratorRepository;
import br.com.raroacademy.demo.client.ViaCepClient;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CollaboratorService {

    private final CollaboratorRepository collaboratorRepository;
    private final MapperCollaborator mapperCollaborator;
    private final MapperAddress mapperAddress;
    private final AddressRepository addressRepository;
    private final ViaCepClient viaCepClient;
    private final MessageSource messageSource;

    private String getMessage(String code) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(code, null, locale);
    }

    public CollaboratorResponseDTO save(CollaboratorRequestDTO request) {
        if (collaboratorRepository.existsByCpf(request.getCpf())) {
            throw new DataIntegrityViolationException(getMessage("collaborator.cpf.already-exists"));
        }

        if (collaboratorRepository.existsByEmail(request.getEmail())) {
            throw new DataIntegrityViolationException(getMessage("collaborator.email.already-exists"));
        }

        var viaCep = viaCepClient.buscarEnderecoPorCep(request.getCep());
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

        var viaCep = viaCepClient.buscarEnderecoPorCep(dto.getCep());
        if (viaCep.getCep() == null) {
            throw new InvalidCepException(getMessage("address.cep.invalid"));
        }

        address.setCep(viaCep.getCep());
        address.setStreet(viaCep.getStreet());
        address.setNeighborhood(viaCep.getNeighborhood());
        address.setCity(viaCep.getCity());
        address.setState(viaCep.getState());
        address.setNumber(dto.getNumber());
        address.setComplement(dto.getComplement());

        addressRepository.save(address);

        existing.setName(dto.getName());
        existing.setEmail(dto.getEmail());
        existing.setPhone(dto.getPhone());
        existing.setContractStartDate(dto.getContractStartDate());
        existing.setContractEndDate(dto.getContractEndDate());

        CollaboratorEntity updated = collaboratorRepository.save(existing);
        return mapperCollaborator.toResponse(updated, mapperAddress.toDTO(address));
    }

    public void delete(Long id) {
        if (!collaboratorRepository.existsById(id)) {
            throw new NotFoundException(getMessage("collaborator.not-found"));
        }
        collaboratorRepository.deleteById(id);
    }
}
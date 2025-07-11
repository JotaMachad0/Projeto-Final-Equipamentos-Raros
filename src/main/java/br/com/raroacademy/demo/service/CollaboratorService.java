package br.com.raroacademy.demo.service;

import br.com.raroacademy.demo.domain.DTO.address.MapperAddress;
import br.com.raroacademy.demo.domain.DTO.collaborator.CollaboratorRequestDTO;
import br.com.raroacademy.demo.domain.DTO.collaborator.CollaboratorResponseDTO;
import br.com.raroacademy.demo.domain.DTO.collaborator.MapperCollaborator;
import br.com.raroacademy.demo.domain.entities.AddressEntity;
import br.com.raroacademy.demo.domain.entities.CollaboratorEntity;
import br.com.raroacademy.demo.exception.InvalidCepException;
import br.com.raroacademy.demo.exception.NotFoundException;
import br.com.raroacademy.demo.exception.UsedCpfException;
import br.com.raroacademy.demo.exception.UsedEmailException;
import br.com.raroacademy.demo.repository.AddressRepository;
import br.com.raroacademy.demo.repository.CollaboratorRepository;
import br.com.raroacademy.demo.viaCep.ViaCepClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CollaboratorService {

    private CollaboratorRepository collaboratorRepository;

    private MapperCollaborator mapperCollaborator;

    private MapperAddress mapperAddress;

    private AddressRepository addressRepository;

    private ViaCepClient viaCepClient;

    public CollaboratorResponseDTO save(CollaboratorRequestDTO request) {
        if (collaboratorRepository.existsByCpf(request.getCpf())) {
            throw new UsedCpfException("CPF já cadastrado");
        }

        if (collaboratorRepository.existsByEmail(request.getEmail())) {
            throw new UsedEmailException("E-mail já cadastrado");
        }

        var viaCep = viaCepClient.buscarEnderecoPorCep(request.getCep());
        if (viaCep.getCep() == null) {
            throw new InvalidCepException("CEP invalido!");
        }
        var addressEntity = mapperAddress.toEntity(request, viaCep);
        var addressSaved = addressRepository.save(addressEntity);

        var entity = mapperCollaborator.toEntity(request, addressSaved.getId());
        var saved = collaboratorRepository.save(entity);
        var address = mapperAddress.toDTO(addressSaved);
        return mapperCollaborator.toResponse(saved, address);
    }

    public CollaboratorResponseDTO getById(Long id) {
        CollaboratorEntity collabboratorEntity = collaboratorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Colaborador não encontrado"));
        AddressEntity addressEntity = addressRepository.findById(collabboratorEntity.getAddressId())
                .orElseThrow(() -> new NotFoundException("Endereço não encontrado"));
        var address = mapperAddress.toDTO(addressEntity);
        return mapperCollaborator.toResponse(collabboratorEntity, address);
    }

    public List<CollaboratorResponseDTO> getAll() {
        return collaboratorRepository.findAll().stream()
                .map(mapperCollaborator::toResponse)
                .collect(Collectors.toList());
    }
}

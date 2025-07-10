package br.com.raroacademy.demo.service;

import br.com.raroacademy.demo.domain.DTO.address.MapperAddress;
import br.com.raroacademy.demo.domain.DTO.collaborator.CollaboratorRequestDTO;
import br.com.raroacademy.demo.domain.DTO.collaborator.CollaboratorResponseDTO;
import br.com.raroacademy.demo.domain.DTO.collaborator.MapperCollaborator;
import br.com.raroacademy.demo.domain.DTO.viaCep.ViaCepResponseDTO;
import br.com.raroacademy.demo.domain.entities.AddressEntity;
import br.com.raroacademy.demo.domain.entities.CollaboratorEntity;
import br.com.raroacademy.demo.exception.BusinessException;
import br.com.raroacademy.demo.exception.NotFoundException;
import br.com.raroacademy.demo.repository.AddressRepository;
import br.com.raroacademy.demo.repository.CollaboratorRepository;
import br.com.raroacademy.demo.viaCep.ViaCepClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CollaboratorService {

    @Autowired
    private CollaboratorRepository collaboratorRepository;

    @Autowired
    private MapperCollaborator mapperCollaborator;

    @Autowired
    private AddressRepository repository;

    @Autowired
    private ViaCepClient viaCepClient;

    public CollaboratorResponseDTO save(CollaboratorRequestDTO request) {
        if (collaboratorRepository.existsByCpf(request.getCpf())) {
            throw new BusinessException("CPF já cadastrado");
        }

        if (collaboratorRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("E-mail já cadastrado");
        }

        ViaCepResponseDTO viaCep = viaCepClient.buscarEnderecoPorCep(request.getCep());
        AddressEntity addressEntity = MapperAddress.toEntity(request, viaCep);
        AddressEntity addressSaved = repository.save(addressEntity);

        CollaboratorEntity entity = mapperCollaborator.toEntity(request, addressSaved.getId());
        CollaboratorEntity saved = collaboratorRepository.save(entity);
        return mapperCollaborator.toResponse(saved);
    }

    public CollaboratorResponseDTO getById(Long id) {
        CollaboratorEntity entity = collaboratorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Colaborador não encontrado"));
        return mapperCollaborator.toResponse(entity);
    }

    public List<CollaboratorResponseDTO> getAll() {
        return collaboratorRepository.findAll().stream()
                .map(MapperCollaborator::toResponse)
                .collect(Collectors.toList());
    }
}

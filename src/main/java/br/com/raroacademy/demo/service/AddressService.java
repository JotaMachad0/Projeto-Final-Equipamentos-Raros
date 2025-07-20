package br.com.raroacademy.demo.service;

import br.com.raroacademy.demo.domain.DTO.address.AddressResponseDTO;
import br.com.raroacademy.demo.domain.DTO.address.MapperAddress;
import br.com.raroacademy.demo.domain.DTO.collaborator.CollaboratorRequestDTO;
import br.com.raroacademy.demo.domain.DTO.viaCep.ViaCepResponseDTO;
import br.com.raroacademy.demo.domain.entities.AddressEntity;
import br.com.raroacademy.demo.repository.AddressRepository;
import br.com.raroacademy.demo.client.ViaCepClient;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AddressService {

    private AddressRepository repository;

    private ViaCepClient viaCepClient;

    private MapperAddress mapperAddress;

    public AddressResponseDTO save(CollaboratorRequestDTO dto) {
        ViaCepResponseDTO viaCep = viaCepClient.buscarEnderecoPorCep(dto.cep());
        AddressEntity entity = mapperAddress.toEntity(dto, viaCep);
        AddressEntity saved = repository.save(entity);
        return mapperAddress.toDTO(saved);
    }

    public AddressResponseDTO findById(Long id) {
        Optional<AddressEntity> address = repository.findById(id);
        return address.map(mapperAddress::toDTO)
                .orElseThrow(() -> new RuntimeException("Endereço não encontrado"));
    }

    public List<AddressResponseDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(mapperAddress::toDTO)
                .toList();
    }

    public void deleteById(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Endereço não encontrado para o ID: " + id);
        }
        repository.deleteById(id);
    }
}

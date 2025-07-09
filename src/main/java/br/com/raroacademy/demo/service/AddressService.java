package br.com.raroacademy.demo.service;

import br.com.raroacademy.demo.domain.DTO.address.AddressRequestDTO;
import br.com.raroacademy.demo.domain.DTO.address.AddressResponseDTO;
import br.com.raroacademy.demo.domain.DTO.address.MapperAddress;
import br.com.raroacademy.demo.domain.entities.AddressEntity;
import br.com.raroacademy.demo.repository.AddressRepository;
import br.com.raroacademy.demo.viaCep.ViaCepClient;
import br.com.raroacademy.demo.viaCep.ViaCepResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddressService {

    @Autowired
    private AddressRepository repository;

    @Autowired
    private ViaCepClient viaCepClient;

    public AddressResponseDTO save(AddressRequestDTO dto) {
        ViaCepResponseDTO viaCep = viaCepClient.buscarEnderecoPorCep(dto.getCep());
        AddressEntity entity = MapperAddress.toEntity(dto, viaCep);
        AddressEntity saved = repository.save(entity);
        return MapperAddress.toDTO(saved);
    }

    public AddressResponseDTO findById(Long id) {
        Optional<AddressEntity> address = repository.findById(id);
        return address.map(MapperAddress::toDTO)
                .orElseThrow(() -> new RuntimeException("Endereço não encontrado"));
    }

    public List<AddressResponseDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(MapperAddress::toDTO)
                .toList();
    }
}

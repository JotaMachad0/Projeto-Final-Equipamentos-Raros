package br.com.raroacademy.demo.domain.DTO.address;

import br.com.raroacademy.demo.domain.entities.AddressEntity;
import br.com.raroacademy.demo.viaCep.ViaCepResponseDTO;

public class MapperAddress {

    public static AddressEntity toEntity(AddressRequestDTO dto, ViaCepResponseDTO viaCep) {
        return AddressEntity.builder()
                .street(viaCep.getStreet())
                .neighborhood(viaCep.getNeighborhood())
                .city(viaCep.getCity())
                .state(viaCep.getState())
                .cep(dto.getCep())
                .number(dto.getNumber())
                .complement(dto.getComplement())
                .country("Brasil")
                .respostaViaCepJson(viaCep.toString())
                .build();
    }

    public static AddressResponseDTO toDTO(AddressEntity entity) {
        return AddressResponseDTO.builder()
                .id(entity.getId())
                .street(entity.getStreet())
                .neighborhood(entity.getNeighborhood())
                .city(entity.getCity())
                .state(entity.getState())
                .cep(entity.getCep())
                .number(entity.getNumber())
                .complement(entity.getComplement())
                .country(entity.getCountry())
                .build();
    }
}

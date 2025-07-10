package br.com.raroacademy.demo.domain.DTO.address;

import br.com.raroacademy.demo.domain.entities.AddressEntity;
import br.com.raroacademy.demo.viaCep.ViaCepResponseDTO;

public class MapperAddress {

    public static AddressEntity toEntity(AddressRequestDTO dto, ViaCepResponseDTO viaCep) {
        return AddressEntity.builder()
                .street(viaCep.getRua())
                .bairro(viaCep.getBairro())
                .cidade(viaCep.getCidade())
                .estado(viaCep.getEstado())
                .cep(dto.getCep())
                .numero(dto.getNumero())
                .complemento(dto.getComplemento())
                .pais("Brasil")
                .respostaViaCepJson(viaCep.toString())
                .build();
    }

    public static AddressResponseDTO toDTO(AddressEntity entity) {
        return AddressResponseDTO.builder()
                .id(entity.getId())
                .rua(entity.getRua())
                .bairro(entity.getBairro())
                .cidade(entity.getCidade())
                .estado(entity.getEstado())
                .cep(entity.getCep())
                .numero(entity.getNumero())
                .complemento(entity.getComplemento())
                .pais(entity.getPais())
                .build();
    }
}

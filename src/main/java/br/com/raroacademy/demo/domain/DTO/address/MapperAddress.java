package br.com.raroacademy.demo.domain.DTO.address;

import br.com.raroacademy.demo.domain.DTO.collaborator.CollaboratorRequestDTO;
import br.com.raroacademy.demo.domain.DTO.viaCep.ViaCepResponseDTO;
import br.com.raroacademy.demo.domain.entities.AddressEntity;
import org.springframework.stereotype.Component;

@Component
public class MapperAddress {

    public AddressEntity toEntity(CollaboratorRequestDTO dto, ViaCepResponseDTO viaCep) {
        String jsonParaTeste = "{\"cep\": \"01001-000\", \"logradouro\": \"Praça da Sé\", \"complemento\": \"lado ímpar\", \"bairro\": \"Sé\", \"localidade\": \"São Paulo\", \"uf\": \"SP\"}";
        return AddressEntity.builder()
                .street(viaCep.getStreet())
                .neighborhood(viaCep.getNeighborhood())
                .city(viaCep.getCity())
                .state(viaCep.getState())
                .cep(dto.cep())
                .number(dto.number())
                .complement(dto.complement())
                .country("Brasil")
                .respostaViaCepJson(jsonParaTeste)
                .build();
    }

    public AddressResponseDTO toDTO(AddressEntity entity) {
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

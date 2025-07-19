package br.com.raroacademy.demo.domain.DTO.collaborator;

import br.com.raroacademy.demo.domain.DTO.address.AddressResponseDTO;
import br.com.raroacademy.demo.domain.entities.CollaboratorEntity;
import org.springframework.stereotype.Component;

@Component
public class MapperCollaborator {

    public CollaboratorEntity toEntity(CollaboratorRequestDTO dto, Long addressId) {
        return CollaboratorEntity.builder()
                .name(dto.getName())
                .cpf(dto.getCpf())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .addressId(addressId)
                .contractStartDate(dto.getContractStartDate())
                .contractEndDate(dto.getContractEndDate())
                .build();
    }

    public CollaboratorResponseDTO toResponse(CollaboratorEntity entity, AddressResponseDTO addressResponseDto) {
        return CollaboratorResponseDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .cpf(entity.getCpf())
                .email(entity.getEmail())
                .phone(entity.getPhone())
                .contractStartDate(entity.getContractStartDate())
                .contractEndDate(entity.getContractEndDate())
                .address(addressResponseDto)
                .build();
    }

    public CollaboratorResponseDTO toResponse(CollaboratorEntity entity) {
        return CollaboratorResponseDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .cpf(entity.getCpf())
                .email(entity.getEmail())
                .phone(entity.getPhone())
                .contractStartDate(entity.getContractStartDate())
                .contractEndDate(entity.getContractEndDate())
                .build();
    }

    public CollaboratorSummaryDTO toSummaryResponse(CollaboratorEntity entity) {
        return CollaboratorSummaryDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .cpf(entity.getCpf())
                .email(entity.getEmail())
                .build();
    }
}

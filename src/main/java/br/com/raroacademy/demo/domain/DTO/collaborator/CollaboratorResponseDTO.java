package br.com.raroacademy.demo.domain.DTO.collaborator;

import br.com.raroacademy.demo.domain.DTO.address.AddressResponseDTO;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDate;

@Getter
@Builder
public class CollaboratorResponseDTO {
    private Long id;
    private String name;
    private String cpf;
    private String email;
    private String phone;
    private LocalDate contractStartDate;
    private LocalDate contractEndDate;


    private AddressResponseDTO address;
}

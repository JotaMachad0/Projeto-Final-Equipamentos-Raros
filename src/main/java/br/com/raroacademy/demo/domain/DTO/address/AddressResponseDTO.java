package br.com.raroacademy.demo.domain.DTO.address;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddressResponseDTO {

    private Long id;
    private String street;
    private String number;
    private String complement;
    private String neighborhood;
    private String city;
    private String state;
    private String cep;
    private String country;
}

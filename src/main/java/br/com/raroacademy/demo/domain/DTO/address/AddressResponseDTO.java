package br.com.raroacademy.demo.domain.DTO.address;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddressResponseDTO {

    private Long id;
    private String rua;
    private String numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;
    private String pais;
}

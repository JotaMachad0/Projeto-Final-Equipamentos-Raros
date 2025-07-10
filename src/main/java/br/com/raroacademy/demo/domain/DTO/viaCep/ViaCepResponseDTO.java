package br.com.raroacademy.demo.viaCep;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ViaCepResponseDTO {

    private String cep;

    @JsonProperty("logradouro")
    private String street;

    @JsonProperty("bairro")
    private String neighborhood;

    @JsonProperty("localidade")
    private String city;

    @JsonProperty("uf")
    private String state;
}

package br.com.raroacademy.demo.viaCep;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ViaCepResponseDTO {

    private String cep;

    @JsonProperty("logradouro")
    private String rua;

    @JsonProperty("bairro")
    private String bairro;

    @JsonProperty("localidade")
    private String cidade;

    @JsonProperty("uf")
    private String estado;
}

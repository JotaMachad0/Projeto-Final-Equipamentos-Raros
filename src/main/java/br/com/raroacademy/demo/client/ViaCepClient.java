package br.com.raroacademy.demo.client;

import br.com.raroacademy.demo.domain.DTO.viaCep.ViaCepResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "viacep", url = "${feign.viaCep.url}")
public interface ViaCepClient {

    @GetMapping("/{cep}/json/")
    ViaCepResponseDTO buscarEnderecoPorCep(@PathVariable("cep") String cep);
}

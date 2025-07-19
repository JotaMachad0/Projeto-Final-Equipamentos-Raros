package br.com.raroacademy.demo.service;

import br.com.raroacademy.demo.client.ViaCepClient;
import br.com.raroacademy.demo.domain.DTO.viaCep.ViaCepResponseDTO;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@AllArgsConstructor
public class ViaCepService {

    private final ViaCepClient viaCepClient;

    @Async
    public CompletableFuture<ViaCepResponseDTO> buscarEnderecoPorCepAsync(String cep) {
        ViaCepResponseDTO response = viaCepClient.buscarEnderecoPorCep(cep);
        return CompletableFuture.completedFuture(response);
    }
}
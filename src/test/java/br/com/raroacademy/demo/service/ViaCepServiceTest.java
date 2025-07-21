package br.com.raroacademy.demo.service;

import br.com.raroacademy.demo.client.ViaCepClient;
import br.com.raroacademy.demo.domain.DTO.viaCep.ViaCepResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ViaCepServiceTest {

    @Mock
    private ViaCepClient viaCepClient;

    @InjectMocks
    private ViaCepService viaCepService;

    private ViaCepResponseDTO viaCepResponseDTO;
    private final String validCep = "12345678";

    @BeforeEach
    void setUp() {
        viaCepResponseDTO = new ViaCepResponseDTO();
        viaCepResponseDTO.setCep("12345-678");
        viaCepResponseDTO.setStreet("Test Street");
        viaCepResponseDTO.setNeighborhood("Test Neighborhood");
        viaCepResponseDTO.setCity("Test City");
        viaCepResponseDTO.setState("TS");
    }

    @Test
    void buscarEnderecoPorCepAsync_Success() throws ExecutionException, InterruptedException {
        // Arrange
        when(viaCepClient.buscarEnderecoPorCep(validCep)).thenReturn(viaCepResponseDTO);

        // Act
        CompletableFuture<ViaCepResponseDTO> result = viaCepService.buscarEnderecoPorCepAsync(validCep);

        // Assert
        assertNotNull(result);
        ViaCepResponseDTO response = result.get();
        assertEquals(viaCepResponseDTO, response);
        verify(viaCepClient).buscarEnderecoPorCep(validCep);
    }

    @Test
    void buscarEnderecoPorCepAsync_ClientThrowsException() {
        // Arrange
        when(viaCepClient.buscarEnderecoPorCep(validCep)).thenThrow(new RuntimeException("API error"));

        // Act & Assert
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> viaCepService.buscarEnderecoPorCepAsync(validCep)
        );
        assertEquals("API error", exception.getMessage());
        verify(viaCepClient).buscarEnderecoPorCep(validCep);
    }

    @Test
    void buscarEnderecoPorCepAsync_WithInvalidCep() throws ExecutionException, InterruptedException {
        // Arrange
        String invalidCep = "invalid";
        ViaCepResponseDTO emptyResponse = new ViaCepResponseDTO();
        when(viaCepClient.buscarEnderecoPorCep(invalidCep)).thenReturn(emptyResponse);

        // Act
        CompletableFuture<ViaCepResponseDTO> result = viaCepService.buscarEnderecoPorCepAsync(invalidCep);

        // Assert
        assertNotNull(result);
        ViaCepResponseDTO response = result.get();
        assertEquals(emptyResponse, response);
        verify(viaCepClient).buscarEnderecoPorCep(invalidCep);
    }

    @Test
    void buscarEnderecoPorCepAsync_WithFormattedCep() throws ExecutionException, InterruptedException {
        // Arrange
        String formattedCep = "12345-678";
        when(viaCepClient.buscarEnderecoPorCep(formattedCep)).thenReturn(viaCepResponseDTO);

        // Act
        CompletableFuture<ViaCepResponseDTO> result = viaCepService.buscarEnderecoPorCepAsync(formattedCep);

        // Assert
        assertNotNull(result);
        ViaCepResponseDTO response = result.get();
        assertEquals(viaCepResponseDTO, response);
        verify(viaCepClient).buscarEnderecoPorCep(formattedCep);
    }

    @Test
    void buscarEnderecoPorCepAsync_WithNullCep() {
        // Arrange & Act & Assert
        assertThrows(
                NullPointerException.class,
                () -> viaCepService.buscarEnderecoPorCepAsync(null)
        );
        verify(viaCepClient, never()).buscarEnderecoPorCep(any());
    }
}
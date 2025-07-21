package br.com.raroacademy.demo.service;

import br.com.raroacademy.demo.client.ViaCepClient;
import br.com.raroacademy.demo.domain.DTO.address.AddressResponseDTO;
import br.com.raroacademy.demo.domain.DTO.address.MapperAddress;
import br.com.raroacademy.demo.domain.DTO.collaborator.CollaboratorRequestDTO;
import br.com.raroacademy.demo.domain.DTO.viaCep.ViaCepResponseDTO;
import br.com.raroacademy.demo.domain.entities.AddressEntity;
import br.com.raroacademy.demo.repository.AddressRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AddressServiceTest {

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private ViaCepClient viaCepClient;

    @Mock
    private MapperAddress mapperAddress;

    @InjectMocks
    private AddressService addressService;

    private AddressEntity addressEntity;
    private CollaboratorRequestDTO collaboratorRequestDTO;
    private AddressResponseDTO addressResponseDTO;
    private ViaCepResponseDTO viaCepResponseDTO;

    @BeforeEach
    void setUp() {
        addressEntity = AddressEntity.builder()
                .id(1L)
                .street("Test Street")
                .neighborhood("Test Neighborhood")
                .city("Test City")
                .state("TS")
                .cep("12345-678")
                .number("123")
                .complement("Test Complement")
                .country("Test Country")
                .build();

        collaboratorRequestDTO = CollaboratorRequestDTO.builder()
                .name("Test Collaborator")
                .cpf("123.456.789-00")
                .email("collaborator@example.com")
                .phone("1234567890")
                .contractStartDate(LocalDate.of(2023, 1, 1))
                .contractEndDate(LocalDate.of(2024, 1, 1))
                .cep("12345-678")
                .number("123")
                .complement("Test Complement")
                .build();

        addressResponseDTO = AddressResponseDTO.builder()
                .id(1L)
                .street("Test Street")
                .neighborhood("Test Neighborhood")
                .city("Test City")
                .state("TS")
                .cep("12345-678")
                .number("123")
                .complement("Test Complement")
                .country("Test Country")
                .build();

        viaCepResponseDTO = new ViaCepResponseDTO();
        viaCepResponseDTO.setCep("12345-678");
        viaCepResponseDTO.setStreet("Test Street");
        viaCepResponseDTO.setNeighborhood("Test Neighborhood");
        viaCepResponseDTO.setCity("Test City");
        viaCepResponseDTO.setState("TS");
    }

    @Test
    void save_Success() {
        // Arrange
        when(viaCepClient.buscarEnderecoPorCep(collaboratorRequestDTO.cep())).thenReturn(viaCepResponseDTO);
        when(mapperAddress.toEntity(eq(collaboratorRequestDTO), any(ViaCepResponseDTO.class))).thenReturn(addressEntity);
        when(addressRepository.save(addressEntity)).thenReturn(addressEntity);
        when(mapperAddress.toDTO(addressEntity)).thenReturn(addressResponseDTO);

        // Act
        AddressResponseDTO result = addressService.save(collaboratorRequestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(addressResponseDTO, result);
        verify(viaCepClient).buscarEnderecoPorCep(collaboratorRequestDTO.cep());
        verify(mapperAddress).toEntity(eq(collaboratorRequestDTO), any(ViaCepResponseDTO.class));
        verify(addressRepository).save(addressEntity);
        verify(mapperAddress).toDTO(addressEntity);
    }

    @Test
    void findById_Success() {
        // Arrange
        when(addressRepository.findById(1L)).thenReturn(Optional.of(addressEntity));
        when(mapperAddress.toDTO(addressEntity)).thenReturn(addressResponseDTO);

        // Act
        AddressResponseDTO result = addressService.findById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(addressResponseDTO, result);
        verify(addressRepository).findById(1L);
        verify(mapperAddress).toDTO(addressEntity);
    }

    @Test
    void findById_NotFound() {
        // Arrange
        when(addressRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> addressService.findById(1L)
        );
        assertEquals("Endereço não encontrado", exception.getMessage());
        verify(addressRepository).findById(1L);
    }

    @Test
    void findAll_Success() {
        // Arrange
        List<AddressEntity> addressEntities = Arrays.asList(addressEntity);
        when(addressRepository.findAll()).thenReturn(addressEntities);
        when(mapperAddress.toDTO(addressEntity)).thenReturn(addressResponseDTO);

        // Act
        List<AddressResponseDTO> result = addressService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(addressResponseDTO, result.get(0));
        verify(addressRepository).findAll();
        verify(mapperAddress).toDTO(addressEntity);
    }

    @Test
    void deleteById_Success() {
        // Arrange
        when(addressRepository.existsById(1L)).thenReturn(true);

        // Act
        addressService.deleteById(1L);

        // Assert
        verify(addressRepository).existsById(1L);
        verify(addressRepository).deleteById(1L);
    }

    @Test
    void deleteById_NotFound() {
        // Arrange
        when(addressRepository.existsById(1L)).thenReturn(false);

        // Act & Assert
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> addressService.deleteById(1L)
        );
        assertEquals("Endereço não encontrado para o ID: 1", exception.getMessage());
        verify(addressRepository).existsById(1L);
        verify(addressRepository, never()).deleteById(anyLong());
    }
}
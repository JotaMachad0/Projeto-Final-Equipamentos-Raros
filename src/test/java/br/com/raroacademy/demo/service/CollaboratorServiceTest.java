package br.com.raroacademy.demo.service;

import br.com.raroacademy.demo.client.ViaCepClient;
import br.com.raroacademy.demo.domain.DTO.address.AddressResponseDTO;
import br.com.raroacademy.demo.domain.DTO.address.MapperAddress;
import br.com.raroacademy.demo.domain.DTO.collaborator.CollaboratorRequestDTO;
import br.com.raroacademy.demo.domain.DTO.collaborator.CollaboratorResponseDTO;
import br.com.raroacademy.demo.domain.DTO.collaborator.MapperCollaborator;
import br.com.raroacademy.demo.domain.DTO.viaCep.ViaCepResponseDTO;
import br.com.raroacademy.demo.domain.entities.AddressEntity;
import br.com.raroacademy.demo.domain.entities.CollaboratorEntity;
import br.com.raroacademy.demo.exception.DataIntegrityViolationException;
import br.com.raroacademy.demo.exception.InvalidCepException;
import br.com.raroacademy.demo.exception.NotFoundException;
import br.com.raroacademy.demo.repository.AddressRepository;
import br.com.raroacademy.demo.repository.CollaboratorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.context.MessageSource;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class CollaboratorServiceTest {

    @Mock
    private CollaboratorRepository collaboratorRepository;

    @Mock
    private MapperCollaborator mapperCollaborator;

    @Mock
    private MapperAddress mapperAddress;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private ViaCepClient viaCepClient;
    
    @Mock
    private ViaCepService viaCepService;

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private CollaboratorService collaboratorService;

    private CollaboratorEntity collaboratorEntity;
    private AddressEntity addressEntity;
    private CollaboratorRequestDTO collaboratorRequestDTO;
    private CollaboratorResponseDTO collaboratorResponseDTO;
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

        collaboratorEntity = CollaboratorEntity.builder()
                .id(1L)
                .name("Test Collaborator")
                .cpf("123.456.789-00")
                .email("collaborator@example.com")
                .phone("1234567890")
                .addressId(1L)
                .contractStartDate(LocalDate.of(2023, 1, 1))
                .contractEndDate(LocalDate.of(2024, 1, 1))
                .build();

        collaboratorRequestDTO = CollaboratorRequestDTO.builder()
                .name("Test Collaborator")
                .cpf("123.456.789-00")
                .email("collaborator@example.com")
                .phone("1234567890")
                .addressId(null)
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

        collaboratorResponseDTO = CollaboratorResponseDTO.builder()
                .id(1L)
                .name("Test Collaborator")
                .cpf("123.456.789-00")
                .email("collaborator@example.com")
                .phone("1234567890")
                .contractStartDate(LocalDate.of(2023, 1, 1))
                .contractEndDate(LocalDate.of(2024, 1, 1))
                .address(addressResponseDTO)
                .build();

        viaCepResponseDTO = new ViaCepResponseDTO();
        viaCepResponseDTO.setCep("12345-678");
        viaCepResponseDTO.setStreet("Test Street");
        viaCepResponseDTO.setNeighborhood("Test Neighborhood");
        viaCepResponseDTO.setCity("Test City");
        viaCepResponseDTO.setState("TS");

        when(messageSource.getMessage(eq("collaborator.not-found"), any(), any(Locale.class)))
                .thenReturn("Collaborator not found");
        when(messageSource.getMessage(eq("address.not-found"), any(), any(Locale.class)))
                .thenReturn("Address not found");
        when(messageSource.getMessage(eq("collaborator.cpf.already-exists"), any(), any(Locale.class)))
                .thenReturn("CPF already exists");
        when(messageSource.getMessage(eq("collaborator.email.already-exists"), any(), any(Locale.class)))
                .thenReturn("Email already exists");
        when(messageSource.getMessage(eq("address.cep.invalid"), any(), any(Locale.class)))
                .thenReturn("Invalid CEP");
    }

    @Test
    void save_Success() {
        // Arrange
        when(collaboratorRepository.existsByCpf(collaboratorRequestDTO.cpf())).thenReturn(false);
        when(collaboratorRepository.existsByEmail(collaboratorRequestDTO.email())).thenReturn(false);
        
        // Mock the async ViaCep service
        CompletableFuture<ViaCepResponseDTO> future = CompletableFuture.completedFuture(viaCepResponseDTO);
        when(viaCepService.buscarEnderecoPorCepAsync(collaboratorRequestDTO.cep())).thenReturn(future);
        
        when(mapperAddress.toEntity(eq(collaboratorRequestDTO), any(ViaCepResponseDTO.class))).thenReturn(addressEntity);
        when(addressRepository.save(addressEntity)).thenReturn(addressEntity);
        when(mapperCollaborator.toEntity(collaboratorRequestDTO, addressEntity.getId())).thenReturn(collaboratorEntity);
        when(collaboratorRepository.save(collaboratorEntity)).thenReturn(collaboratorEntity);
        when(mapperAddress.toDTO(addressEntity)).thenReturn(addressResponseDTO);
        when(mapperCollaborator.toResponse(collaboratorEntity, addressResponseDTO)).thenReturn(collaboratorResponseDTO);

        // Act
        CollaboratorResponseDTO result = collaboratorService.save(collaboratorRequestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(collaboratorResponseDTO, result);
        verify(collaboratorRepository).existsByCpf(collaboratorRequestDTO.cpf());
        verify(collaboratorRepository).existsByEmail(collaboratorRequestDTO.email());
        verify(viaCepService).buscarEnderecoPorCepAsync(collaboratorRequestDTO.cep());
        verify(mapperAddress).toEntity(eq(collaboratorRequestDTO), any(ViaCepResponseDTO.class));
        verify(addressRepository).save(addressEntity);
        verify(mapperCollaborator).toEntity(collaboratorRequestDTO, addressEntity.getId());
        verify(collaboratorRepository).save(collaboratorEntity);
        verify(mapperAddress).toDTO(addressEntity);
        verify(mapperCollaborator).toResponse(collaboratorEntity, addressResponseDTO);
    }

    @Test
    void save_CpfAlreadyExists() {
        // Arrange
        when(collaboratorRepository.existsByCpf(collaboratorRequestDTO.cpf())).thenReturn(true);
        
        // Mock the async ViaCep service - it's called before the CPF check
        CompletableFuture<ViaCepResponseDTO> future = CompletableFuture.completedFuture(viaCepResponseDTO);
        when(viaCepService.buscarEnderecoPorCepAsync(collaboratorRequestDTO.cep())).thenReturn(future);

        // Act & Assert
        DataIntegrityViolationException exception = assertThrows(
                DataIntegrityViolationException.class,
                () -> collaboratorService.save(collaboratorRequestDTO)
        );
        assertEquals("CPF already exists", exception.getMessage());
        verify(collaboratorRepository).existsByCpf(collaboratorRequestDTO.cpf());
        verify(collaboratorRepository, never()).existsByEmail(anyString());
        verify(addressRepository, never()).save(any());
        verify(collaboratorRepository, never()).save(any());
    }

    @Test
    void save_EmailAlreadyExists() {
        // Arrange
        when(collaboratorRepository.existsByCpf(collaboratorRequestDTO.cpf())).thenReturn(false);
        when(collaboratorRepository.existsByEmail(collaboratorRequestDTO.email())).thenReturn(true);
        
        // Mock the async ViaCep service - it's called before the email check
        CompletableFuture<ViaCepResponseDTO> future = CompletableFuture.completedFuture(viaCepResponseDTO);
        when(viaCepService.buscarEnderecoPorCepAsync(collaboratorRequestDTO.cep())).thenReturn(future);

        // Act & Assert
        DataIntegrityViolationException exception = assertThrows(
                DataIntegrityViolationException.class,
                () -> collaboratorService.save(collaboratorRequestDTO)
        );
        assertEquals("Email already exists", exception.getMessage());
        verify(collaboratorRepository).existsByCpf(collaboratorRequestDTO.cpf());
        verify(collaboratorRepository).existsByEmail(collaboratorRequestDTO.email());
        verify(addressRepository, never()).save(any());
        verify(collaboratorRepository, never()).save(any());
    }

    @Test
    void save_InvalidCep() {
        // Arrange
        when(collaboratorRepository.existsByCpf(collaboratorRequestDTO.cpf())).thenReturn(false);
        when(collaboratorRepository.existsByEmail(collaboratorRequestDTO.email())).thenReturn(false);
        
        ViaCepResponseDTO invalidCepResponse = new ViaCepResponseDTO();
        invalidCepResponse.setCep(null);
        

        CompletableFuture<ViaCepResponseDTO> future = CompletableFuture.completedFuture(invalidCepResponse);
        when(viaCepService.buscarEnderecoPorCepAsync(collaboratorRequestDTO.cep())).thenReturn(future);

        // Act & Assert
        InvalidCepException exception = assertThrows(
                InvalidCepException.class,
                () -> collaboratorService.save(collaboratorRequestDTO)
        );
        assertEquals("Invalid CEP", exception.getMessage());
        verify(collaboratorRepository).existsByCpf(collaboratorRequestDTO.cpf());
        verify(collaboratorRepository).existsByEmail(collaboratorRequestDTO.email());
        verify(viaCepService).buscarEnderecoPorCepAsync(collaboratorRequestDTO.cep());
        verify(addressRepository, never()).save(any());
        verify(collaboratorRepository, never()).save(any());
    }

    @Test
    void getById_Success() {
        // Arrange
        when(collaboratorRepository.findById(1L)).thenReturn(Optional.of(collaboratorEntity));
        when(addressRepository.findById(collaboratorEntity.getAddressId())).thenReturn(Optional.of(addressEntity));
        when(mapperAddress.toDTO(addressEntity)).thenReturn(addressResponseDTO);
        when(mapperCollaborator.toResponse(collaboratorEntity, addressResponseDTO)).thenReturn(collaboratorResponseDTO);

        // Act
        CollaboratorResponseDTO result = collaboratorService.getById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(collaboratorResponseDTO, result);
        verify(collaboratorRepository).findById(1L);
        verify(addressRepository).findById(collaboratorEntity.getAddressId());
        verify(mapperAddress).toDTO(addressEntity);
        verify(mapperCollaborator).toResponse(collaboratorEntity, addressResponseDTO);
    }

    @Test
    void getById_CollaboratorNotFound() {
        // Arrange
        when(collaboratorRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> collaboratorService.getById(1L)
        );
        assertEquals("Collaborator not found", exception.getMessage());
        verify(collaboratorRepository).findById(1L);
        verify(addressRepository, never()).findById(anyLong());
        verify(mapperAddress, never()).toDTO(any());
        verify(mapperCollaborator, never()).toResponse(any(), any());
    }

    @Test
    void getById_AddressNotFound() {
        // Arrange
        when(collaboratorRepository.findById(1L)).thenReturn(Optional.of(collaboratorEntity));
        when(addressRepository.findById(collaboratorEntity.getAddressId())).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> collaboratorService.getById(1L)
        );
        assertEquals("Address not found", exception.getMessage());
        verify(collaboratorRepository).findById(1L);
        verify(addressRepository).findById(collaboratorEntity.getAddressId());
        verify(mapperAddress, never()).toDTO(any());
        verify(mapperCollaborator, never()).toResponse(any(), any());
    }

    @Test
    void getAll_Success() {
        // Arrange
        List<CollaboratorEntity> collaboratorEntities = Arrays.asList(collaboratorEntity);
        when(collaboratorRepository.findAll()).thenReturn(collaboratorEntities);
        when(addressRepository.findById(collaboratorEntity.getAddressId())).thenReturn(Optional.of(addressEntity));
        when(mapperAddress.toDTO(addressEntity)).thenReturn(addressResponseDTO);
        when(mapperCollaborator.toResponse(collaboratorEntity, addressResponseDTO)).thenReturn(collaboratorResponseDTO);

        // Act
        List<CollaboratorResponseDTO> result = collaboratorService.getAll();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(collaboratorResponseDTO, result.get(0));
        verify(collaboratorRepository).findAll();
        verify(addressRepository).findById(collaboratorEntity.getAddressId());
        verify(mapperAddress).toDTO(addressEntity);
        verify(mapperCollaborator).toResponse(collaboratorEntity, addressResponseDTO);
    }

    @Test
    void update_Success() {
        // Arrange
        when(collaboratorRepository.findById(1L)).thenReturn(Optional.of(collaboratorEntity));
        when(addressRepository.findById(collaboratorEntity.getAddressId())).thenReturn(Optional.of(addressEntity));
        when(viaCepClient.buscarEnderecoPorCep(collaboratorRequestDTO.cep())).thenReturn(viaCepResponseDTO);
        when(addressRepository.save(addressEntity)).thenReturn(addressEntity);
        when(collaboratorRepository.save(collaboratorEntity)).thenReturn(collaboratorEntity);
        when(mapperAddress.toDTO(addressEntity)).thenReturn(addressResponseDTO);
        when(mapperCollaborator.toResponse(collaboratorEntity, addressResponseDTO)).thenReturn(collaboratorResponseDTO);

        // Act
        CollaboratorResponseDTO result = collaboratorService.update(1L, collaboratorRequestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(collaboratorResponseDTO, result);
        verify(collaboratorRepository).findById(1L);
        verify(addressRepository).findById(collaboratorEntity.getAddressId());
        verify(viaCepClient).buscarEnderecoPorCep(collaboratorRequestDTO.cep());
        verify(addressRepository).save(addressEntity);
        verify(collaboratorRepository).save(collaboratorEntity);
        verify(mapperAddress).toDTO(addressEntity);
        verify(mapperCollaborator).toResponse(collaboratorEntity, addressResponseDTO);

        assertEquals(viaCepResponseDTO.getCep(), addressEntity.getCep());
        assertEquals(viaCepResponseDTO.getStreet(), addressEntity.getStreet());
        assertEquals(viaCepResponseDTO.getNeighborhood(), addressEntity.getNeighborhood());
        assertEquals(viaCepResponseDTO.getCity(), addressEntity.getCity());
        assertEquals(viaCepResponseDTO.getState(), addressEntity.getState());
        assertEquals(collaboratorRequestDTO.number(), addressEntity.getNumber());
        assertEquals(collaboratorRequestDTO.complement(), addressEntity.getComplement());

        assertEquals(collaboratorRequestDTO.name(), collaboratorEntity.getName());
        assertEquals(collaboratorRequestDTO.email(), collaboratorEntity.getEmail());
        assertEquals(collaboratorRequestDTO.phone(), collaboratorEntity.getPhone());
        assertEquals(collaboratorRequestDTO.contractStartDate(), collaboratorEntity.getContractStartDate());
        assertEquals(collaboratorRequestDTO.contractEndDate(), collaboratorEntity.getContractEndDate());
    }

    @Test
    void update_CollaboratorNotFound() {
        // Arrange
        when(collaboratorRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> collaboratorService.update(1L, collaboratorRequestDTO)
        );
        assertEquals("Collaborator not found", exception.getMessage());
        verify(collaboratorRepository).findById(1L);
        verify(addressRepository, never()).findById(anyLong());
        verify(viaCepClient, never()).buscarEnderecoPorCep(anyString());
        verify(addressRepository, never()).save(any());
        verify(collaboratorRepository, never()).save(any());
    }

    @Test
    void update_AddressNotFound() {
        // Arrange
        when(collaboratorRepository.findById(1L)).thenReturn(Optional.of(collaboratorEntity));
        when(addressRepository.findById(collaboratorEntity.getAddressId())).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> collaboratorService.update(1L, collaboratorRequestDTO)
        );
        assertEquals("Address not found", exception.getMessage());
        verify(collaboratorRepository).findById(1L);
        verify(addressRepository).findById(collaboratorEntity.getAddressId());
        verify(viaCepClient, never()).buscarEnderecoPorCep(anyString());
        verify(addressRepository, never()).save(any());
        verify(collaboratorRepository, never()).save(any());
    }

    @Test
    void update_InvalidCep() {
        // Arrange
        when(collaboratorRepository.findById(1L)).thenReturn(Optional.of(collaboratorEntity));
        when(addressRepository.findById(collaboratorEntity.getAddressId())).thenReturn(Optional.of(addressEntity));

        ViaCepResponseDTO invalidCepResponse = new ViaCepResponseDTO();
        invalidCepResponse.setCep(null);

        when(viaCepClient.buscarEnderecoPorCep(collaboratorRequestDTO.cep())).thenReturn(invalidCepResponse);

        // Act & Assert
        InvalidCepException exception = assertThrows(
                InvalidCepException.class,
                () -> collaboratorService.update(1L, collaboratorRequestDTO)
        );
        assertEquals("Invalid CEP", exception.getMessage());
        verify(collaboratorRepository).findById(1L);
        verify(addressRepository).findById(collaboratorEntity.getAddressId());
        verify(viaCepClient).buscarEnderecoPorCep(collaboratorRequestDTO.cep());
        verify(addressRepository, never()).save(any());
        verify(collaboratorRepository, never()).save(any());
    }

    @Test
    void delete_Success() {
        // Arrange
        when(collaboratorRepository.existsById(1L)).thenReturn(true);

        // Act
        collaboratorService.delete(1L);

        // Assert
        verify(collaboratorRepository).existsById(1L);
        verify(collaboratorRepository).deleteById(1L);
    }

    @Test
    void delete_CollaboratorNotFound() {
        // Arrange
        when(collaboratorRepository.existsById(1L)).thenReturn(false);

        // Act & Assert
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> collaboratorService.delete(1L)
        );
        assertEquals("Collaborator not found", exception.getMessage());
        verify(collaboratorRepository).existsById(1L);
        verify(collaboratorRepository, never()).deleteById(anyLong());
    }
}
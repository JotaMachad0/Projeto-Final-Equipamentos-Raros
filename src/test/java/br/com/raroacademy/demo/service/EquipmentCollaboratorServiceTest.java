package br.com.raroacademy.demo.service;

import br.com.raroacademy.demo.commons.i18n.I18nUtil;
import br.com.raroacademy.demo.domain.DTO.collaborator.CollaboratorSummaryDTO;
import br.com.raroacademy.demo.domain.DTO.collaborator.MapperCollaborator;
import br.com.raroacademy.demo.domain.DTO.equipment.EquipmentSummaryDTO;
import br.com.raroacademy.demo.domain.DTO.equipment.MapperEquipment;
import br.com.raroacademy.demo.domain.DTO.equipment.collaborator.EquipmentCollaboratorRequestDTO;
import br.com.raroacademy.demo.domain.DTO.equipment.collaborator.EquipmentCollaboratorResponseDTO;
import br.com.raroacademy.demo.domain.DTO.equipment.collaborator.MapperEquipmentCollaborator;
import br.com.raroacademy.demo.domain.DTO.equipment.collaborator.NewCollaboratorEquipmentLinkRequestDTO;
import br.com.raroacademy.demo.domain.entities.AddressEntity;
import br.com.raroacademy.demo.domain.entities.CollaboratorEntity;
import br.com.raroacademy.demo.domain.entities.EquipmentCollaboratorEntity;
import br.com.raroacademy.demo.domain.entities.EquipmentEntity;
import br.com.raroacademy.demo.domain.enums.EquipmentStatus;
import br.com.raroacademy.demo.domain.enums.EquipmentType;
import br.com.raroacademy.demo.exception.DataIntegrityException;
import br.com.raroacademy.demo.exception.NotFoundException;
import br.com.raroacademy.demo.repository.AddressRepository;
import br.com.raroacademy.demo.repository.CollaboratorRepository;
import br.com.raroacademy.demo.repository.EquipmentCollaboratorRepository;
import br.com.raroacademy.demo.repository.EquipmentRepository;
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
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class EquipmentCollaboratorServiceTest {

    @Mock
    private EquipmentCollaboratorRepository equipmentCollaboratorRepository;
    @Mock
    private CollaboratorRepository collaboratorRepository;
    @Mock
    private EquipmentRepository equipmentRepository;
    @Mock
    private AddressRepository addressRepository;
    @Mock
    private StockService stockService;
    @Mock
    private MapperEquipmentCollaborator mapperEquipmentCollaborator;
    @Mock
    private MapperCollaborator mapperCollaborator;
    @Mock
    private MapperEquipment mapperEquipment;
    @Mock
    private DeliveryTimeService deliveryTimeService;
    @Mock
    private MessageSource messageSource;
    @Mock
    private I18nUtil i18n;

    @InjectMocks
    private EquipmentCollaboratorService equipmentCollaboratorService;

    private CollaboratorEntity collaborator;
    private EquipmentEntity availableEquipment;
    private EquipmentEntity inUseEquipment;
    private AddressEntity address;
    private EquipmentCollaboratorEntity equipmentCollaboratorEntity;
    private NewCollaboratorEquipmentLinkRequestDTO newLinkRequestDTO;
    private EquipmentCollaboratorRequestDTO updateRequestDTO;
    private EquipmentCollaboratorResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        collaborator = CollaboratorEntity.builder().id(1L).name("Colaborador Teste").addressId(1L).contractEndDate(null).build();
        availableEquipment = EquipmentEntity.builder().id(1L).status(EquipmentStatus.AVAILABLE).type(EquipmentType.NOTEBOOK).build();
        inUseEquipment = EquipmentEntity.builder().id(2L).status(EquipmentStatus.IN_USE).type(EquipmentType.MONITOR).build();
        address = AddressEntity.builder().id(1L).state("SP").build();
        equipmentCollaboratorEntity = EquipmentCollaboratorEntity.builder().id(1L).collaborator(collaborator).equipment(availableEquipment).deliveryDate(LocalDate.now()).build();

        newLinkRequestDTO = new NewCollaboratorEquipmentLinkRequestDTO(1L, 1L, LocalDate.now(), "ENVIADO", "Notas de envio");
        updateRequestDTO = new EquipmentCollaboratorRequestDTO(1L, 1L, LocalDate.now(), null, "Entregue", "Sem notas");

        CollaboratorSummaryDTO collaboratorSummary = CollaboratorSummaryDTO.builder().id(1L).name("Colaborador Teste").build();
        EquipmentSummaryDTO equipmentSummary = EquipmentSummaryDTO.builder().id(1L).type("NOTEBOOK").build();

        responseDTO = new EquipmentCollaboratorResponseDTO(1L, LocalDate.now(), LocalDate.now().plusDays(5), null, "ENVIADO", "Notas", collaboratorSummary, equipmentSummary);

        when(i18n.getMessage(anyString())).thenAnswer(invocation -> invocation.getArgument(0));
        when(messageSource.getMessage(anyString(), any(), any(Locale.class))).thenAnswer(invocation -> invocation.getArgument(0));

        when(collaboratorRepository.findById(1L)).thenReturn(Optional.of(collaborator));
        when(equipmentRepository.findById(1L)).thenReturn(Optional.of(availableEquipment));
        when(equipmentRepository.findById(2L)).thenReturn(Optional.of(inUseEquipment));
        when(addressRepository.findById(1L)).thenReturn(Optional.of(address));
        when(equipmentCollaboratorRepository.findById(1L)).thenReturn(Optional.of(equipmentCollaboratorEntity));

        when(mapperEquipmentCollaborator.toEntity(any(NewCollaboratorEquipmentLinkRequestDTO.class), any(CollaboratorEntity.class), any(EquipmentEntity.class))).thenReturn(equipmentCollaboratorEntity);
        when(mapperEquipmentCollaborator.toResponse(any(EquipmentCollaboratorEntity.class), any(CollaboratorSummaryDTO.class), any(EquipmentSummaryDTO.class))).thenReturn(responseDTO);
        when(mapperEquipmentCollaborator.mapToResponseDTO(any(EquipmentCollaboratorEntity.class))).thenReturn(responseDTO);

        when(mapperCollaborator.toSummaryResponse(any(CollaboratorEntity.class))).thenReturn(collaboratorSummary);
        when(mapperEquipment.toSummaryResponse(any(EquipmentEntity.class))).thenReturn(equipmentSummary);

        when(deliveryTimeService.calculate(anyString(), any(LocalDate.class))).thenReturn(LocalDate.now().plusDays(5));
        when(equipmentCollaboratorRepository.save(any(EquipmentCollaboratorEntity.class))).thenReturn(equipmentCollaboratorEntity);
    }

    @Test
    void create_Success() {
        EquipmentCollaboratorResponseDTO result = equipmentCollaboratorService.create(newLinkRequestDTO);

        assertNotNull(result);
        assertEquals(responseDTO.id(), result.id());
        verify(stockService).decrementStock(EquipmentType.NOTEBOOK);
        verify(equipmentRepository).save(availableEquipment);
        assertEquals(EquipmentStatus.IN_USE, availableEquipment.getStatus());
    }

    @Test
    void create_CollaboratorNotFound() {
        when(collaboratorRepository.findById(1L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> equipmentCollaboratorService.create(newLinkRequestDTO));
        assertEquals("collaborator.not-found", exception.getMessage());
    }

    @Test
    void create_CollaboratorDismissed() {
        collaborator.setContractEndDate(LocalDate.now());
        when(collaboratorRepository.findById(1L)).thenReturn(Optional.of(collaborator));

        DataIntegrityException exception = assertThrows(DataIntegrityException.class, () -> {
            equipmentCollaboratorService.create(newLinkRequestDTO);
        });

        assertEquals("collaborator.dismissed", exception.getMessage());
    }

    @Test
    void create_EquipmentNotFound() {
        when(equipmentRepository.findById(1L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> equipmentCollaboratorService.create(newLinkRequestDTO));
        assertEquals("equipment.not-found", exception.getMessage());
    }

    @Test
    void create_EquipmentNotAvailable() {
        NewCollaboratorEquipmentLinkRequestDTO requestWithUnavailableEquipment = new NewCollaboratorEquipmentLinkRequestDTO(1L, 2L, LocalDate.now(), "TENTATIVA", "Notas");

        DataIntegrityException exception = assertThrows(DataIntegrityException.class, () -> equipmentCollaboratorService.create(requestWithUnavailableEquipment));
        assertEquals("equipment.unavailable.status", exception.getMessage());
    }

    @Test
    void create_AddressNotFound() {
        when(addressRepository.findById(1L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> equipmentCollaboratorService.create(newLinkRequestDTO));
        assertEquals("address.not-found", exception.getMessage());
    }

    @Test
    void getById_Success() {
        EquipmentCollaboratorResponseDTO result = equipmentCollaboratorService.getById(1L);
        assertNotNull(result);
        assertEquals(1L, result.id());
    }

    @Test
    void getById_NotFound() {
        when(equipmentCollaboratorRepository.findById(1L)).thenReturn(Optional.empty());
        NotFoundException exception = assertThrows(NotFoundException.class, () -> equipmentCollaboratorService.getById(1L));
        assertEquals("equipment-collaborator.not-found", exception.getMessage());
    }

    @Test
    void getAll_Success() {
        when(equipmentCollaboratorRepository.findAll()).thenReturn(Collections.singletonList(equipmentCollaboratorEntity));
        List<EquipmentCollaboratorResponseDTO> result = equipmentCollaboratorService.getAll();
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void update_SuccessWithReturn() {
        updateRequestDTO = new EquipmentCollaboratorRequestDTO(1L, 1L, LocalDate.now(), LocalDate.now(), "Devolvido", "Devolvido");

        EquipmentCollaboratorResponseDTO result = equipmentCollaboratorService.update(1L, updateRequestDTO);

        assertNotNull(result);
        verify(stockService).incrementStock(EquipmentType.NOTEBOOK);
        verify(equipmentRepository).save(availableEquipment);
        assertEquals(EquipmentStatus.AVAILABLE, availableEquipment.getStatus());
    }

    @Test
    void update_ReturnDateInFuture() {
        updateRequestDTO = new EquipmentCollaboratorRequestDTO(1L, 1L, LocalDate.now(), LocalDate.now().plusDays(1), "Agendado", "Agendado");

        DataIntegrityException exception = assertThrows(DataIntegrityException.class, () -> equipmentCollaboratorService.update(1L, updateRequestDTO));
        assertEquals("return.date.future", exception.getMessage());
    }

    @Test
    void delete_Success() {
        when(equipmentCollaboratorRepository.existsById(1L)).thenReturn(true);
        equipmentCollaboratorService.delete(1L);
        verify(equipmentCollaboratorRepository).deleteById(1L);
    }

    @Test
    void delete_NotFound() {
        when(equipmentCollaboratorRepository.existsById(1L)).thenReturn(false);
        NotFoundException exception = assertThrows(NotFoundException.class, () -> equipmentCollaboratorService.delete(1L));
        assertEquals("equipment-collaborator.not-found", exception.getMessage());
    }

    @Test
    void finalizeLoan_Success() {
        when(equipmentCollaboratorRepository.existsById(1L)).thenReturn(true);
        equipmentCollaboratorService.finalizeLoan(1L);
        verify(equipmentCollaboratorRepository).deleteById(1L);
    }

    @Test
    void finalizeLoan_NotFound() {
        when(equipmentCollaboratorRepository.existsById(1L)).thenReturn(false);
        NotFoundException exception = assertThrows(NotFoundException.class, () -> equipmentCollaboratorService.finalizeLoan(1L));
        assertEquals("equipment-collaborator.not-found", exception.getMessage());
    }
}
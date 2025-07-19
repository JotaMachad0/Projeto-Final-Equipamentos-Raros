package br.com.raroacademy.demo.service;

import br.com.raroacademy.demo.domain.DTO.equipmentPurchase.EquipmentPurchasesRequestDTO;
import br.com.raroacademy.demo.domain.DTO.equipmentPurchase.EquipmentPurchasesResponseDTO;
import br.com.raroacademy.demo.domain.DTO.equipmentPurchase.MapperEquipmentPurchases;
import br.com.raroacademy.demo.domain.entities.EquipmentPurchasesEntity;
import br.com.raroacademy.demo.domain.enums.PurchaseStatus;
import br.com.raroacademy.demo.exception.NotFoundException;
import br.com.raroacademy.demo.repository.EquipmentPurchasesRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EquipmentPurchasesService {

    private final EquipmentPurchasesRepository equipmentPurchasesRepository;
    private final MapperEquipmentPurchases mapper;

    @Transactional
    public EquipmentPurchasesResponseDTO create(@Valid EquipmentPurchasesRequestDTO request) {
        var requestWithStatus = new EquipmentPurchasesRequestDTO(
            request.equipmentType(),
            request.quantity(),
            request.orderDate(),
            request.supplier(),
            request.receiptDate(),
            PurchaseStatus.PURCHASED
        );
        
        var entity = mapper.toEntity(requestWithStatus);
        var savedEntity = equipmentPurchasesRepository.save(entity);
        return mapper.toResponseDTO(savedEntity);
    }

    public EquipmentPurchasesResponseDTO getById(Long id) {
        var entity = equipmentPurchasesRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Purchase not found with id: " + id));
        return mapper.toResponseDTO(entity);
    }

    public List<EquipmentPurchasesResponseDTO> getAll() {
        var entities = equipmentPurchasesRepository.findAll();
        return mapper.toResponseDTOList(entities);
    }

    @Transactional
    public EquipmentPurchasesResponseDTO update(Long id, @Valid EquipmentPurchasesRequestDTO request) {
        var existingEntity = equipmentPurchasesRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Purchase not found with id: " + id));

        mapper.updateEntityFromDTO(existingEntity, request);
        var updatedEntity = equipmentPurchasesRepository.save(existingEntity);

        return mapper.toResponseDTO(updatedEntity);
    }

    @Transactional
    public EquipmentPurchasesResponseDTO registerInStock(Long id) {
        EquipmentPurchasesEntity purchase = equipmentPurchasesRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Purchase not found with id: " + id));

        if (PurchaseStatus.REGISTERED.equals(purchase.getStatus())) {
            throw new IllegalStateException("Purchase has already been registered in stock");
        }

        purchase.setStatus(PurchaseStatus.REGISTERED);

        EquipmentPurchasesEntity updatedPurchase = equipmentPurchasesRepository.save(purchase);
        
        return mapper.toResponseDTO(updatedPurchase);
    }
}
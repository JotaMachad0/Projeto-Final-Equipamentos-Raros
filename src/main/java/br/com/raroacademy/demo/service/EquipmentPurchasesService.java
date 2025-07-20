package br.com.raroacademy.demo.service;

import br.com.raroacademy.demo.commons.i18n.I18nUtil;
import br.com.raroacademy.demo.domain.DTO.equipment.purchase.EquipmentPurchasesRequestDTO;
import br.com.raroacademy.demo.domain.DTO.equipment.purchase.EquipmentPurchasesResponseDTO;
import br.com.raroacademy.demo.domain.DTO.equipment.purchase.MapperEquipmentPurchases;
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
    private final I18nUtil i18n;

    @Transactional
    public EquipmentPurchasesResponseDTO create(@Valid EquipmentPurchasesRequestDTO request) {


        var entity = mapper.toEntity(request);
        var savedEntity = equipmentPurchasesRepository.save(entity);
        return mapper.toResponseDTO(savedEntity);
    }

    public EquipmentPurchasesResponseDTO getById(Long id) {
        var entity = equipmentPurchasesRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(i18n.getMessage("purchase.not.found")));

        return mapper.toResponseDTO(entity);
    }

    public List<EquipmentPurchasesResponseDTO> getAll() {
        var entities = equipmentPurchasesRepository.findAll();
        return mapper.toResponseDTOList(entities);
    }

    @Transactional
    public EquipmentPurchasesResponseDTO update(Long id, @Valid EquipmentPurchasesRequestDTO request) {
        var existingEntity = equipmentPurchasesRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(i18n.getMessage("purchase.not.found")));

        mapper.updateEntityFromDTO(existingEntity, request);
        var updatedEntity = equipmentPurchasesRepository.save(existingEntity);

        return mapper.toResponseDTO(updatedEntity);
    }

    @Transactional
    public EquipmentPurchasesResponseDTO registerInStock(Long id) {
        var purchase = equipmentPurchasesRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(i18n.getMessage("purchase.not.found")));

        purchase.setStatus(PurchaseStatus.REGISTERED);
        var updatedPurchase = equipmentPurchasesRepository.save(purchase);
        return mapper.toResponseDTO(updatedPurchase);
    }
}
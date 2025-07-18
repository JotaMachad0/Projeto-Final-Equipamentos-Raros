package br.com.raroacademy.demo.service;

import br.com.raroacademy.demo.commons.i18n.I18nUtil;
import br.com.raroacademy.demo.domain.DTO.stock.EquipmentStockRequestDTO;
import br.com.raroacademy.demo.domain.DTO.stock.EquipmentStockResponseDTO;
import br.com.raroacademy.demo.domain.DTO.stock.MapperEquipmentStock;
import br.com.raroacademy.demo.exception.BusinessException;
import br.com.raroacademy.demo.exception.NotFoundException;
import br.com.raroacademy.demo.repository.EquipmentStockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EquipmentStockService {

    private final EquipmentStockRepository equipmentStockRepository;
    private final MapperEquipmentStock mapperEquipmentStock;
    private final StockAlertService stockAlertService;
    private final I18nUtil i18nUtil;

    @Transactional
    public EquipmentStockResponseDTO create(@Valid EquipmentStockRequestDTO request) {
        var existing = equipmentStockRepository.findByEquipmentType(request.equipmentType());

        if (existing.isPresent()) {
            throw new BusinessException(i18nUtil.getMessage("equipment.stock.already.exists"));
        }

        var equipmentStock = mapperEquipmentStock.toEquipmentStock(request);
        var saved = equipmentStockRepository.save(equipmentStock);
        stockAlertService.checkAndCreateAlert(saved);
        return mapperEquipmentStock.toResponseDTO(saved);
    }

    @Transactional(readOnly = true)
    public EquipmentStockResponseDTO getEquipmentStockById(Long id) {
        var equipmentStock = equipmentStockRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(i18nUtil.getMessage("equipment.stock.not.found")));
        return mapperEquipmentStock.toResponseDTO(equipmentStock);
    }

    @Transactional
    public EquipmentStockResponseDTO update(Long id, @Valid EquipmentStockRequestDTO request) {
        var existing = equipmentStockRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(i18nUtil.getMessage("equipment.stock.not.found")));

        var updated = mapperEquipmentStock.toApplyUpdates(existing, request);

        if (existing.equals(updated)) {
            log.info(i18nUtil.getMessage("equipment.stock.unchanged", existing.getEquipmentType().getLabel()));
            return mapperEquipmentStock.toResponseDTO(existing);
        }

        var saved = equipmentStockRepository.save(updated);
        stockAlertService.checkAndCreateAlert(saved);
        return mapperEquipmentStock.toResponseDTO(saved);
    }

    @Transactional
    public void delete(Long id) {
        var equipmentStock = equipmentStockRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(i18nUtil.getMessage("equipment.stock.not.found")));
        equipmentStockRepository.delete(equipmentStock);
    }

    @Transactional(readOnly = true)
    public List<EquipmentStockResponseDTO> getAllEquipmentStocks() {
        var equipmentStockList = equipmentStockRepository.findAll(
                Sort.by(Sort.Direction.ASC, "equipmentStock.equipmentType")
        );
        return mapperEquipmentStock.toEquipmentStockList(equipmentStockList);
    }
}

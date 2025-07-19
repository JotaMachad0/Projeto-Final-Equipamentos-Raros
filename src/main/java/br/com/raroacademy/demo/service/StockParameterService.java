package br.com.raroacademy.demo.service;

import br.com.raroacademy.demo.commons.i18n.I18nUtil;
import br.com.raroacademy.demo.domain.DTO.stock.parameters.StockParameterRequestDTO;
import br.com.raroacademy.demo.domain.DTO.stock.parameters.StockParameterResponseDTO;
import br.com.raroacademy.demo.domain.DTO.stock.parameters.MapperStockParameter;
import br.com.raroacademy.demo.domain.entities.StockParameterEntity;
import br.com.raroacademy.demo.domain.entities.EquipmentEntity;
import br.com.raroacademy.demo.exception.NotFoundException;
import br.com.raroacademy.demo.repository.EquipmentRepository;
import br.com.raroacademy.demo.repository.StockParameterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockParameterService {

    private final StockParameterRepository stockParameterRepository;
    private final EquipmentRepository equipmentRepository;
    private final I18nUtil i18n;

    private float calculateAvgDefectiveRate(EquipmentEntity equipment) {
        long totalEquipments = equipmentRepository.countByType(equipment.getType());
        long defectiveEquipments = equipmentRepository.countByTypeAndStatus(equipment.getType(), "DEFECTIVE");

        if (totalEquipments == 0) {
            return 0.0f;
        }

        return (float) defectiveEquipments / totalEquipments;
    }

    public StockParameterResponseDTO create(StockParameterRequestDTO request) {
        EquipmentEntity equipment = equipmentRepository.findById(request.getEquipmentId())
                .orElseThrow(() -> new NotFoundException(i18n.getMessage("equipment.not.found", request.getEquipmentId())));

        if (stockParameterRepository.existsByEquipmentId(request.getEquipmentId())) {
            throw new IllegalStateException(i18n.getMessage("stock.parameter.already.exists", request.getEquipmentId()));
        }

        int minStock = equipment.getType().getMinimumStock();
        int securityStock = equipment.getType().getSecurityStock();
        float avgDefectiveRate = calculateAvgDefectiveRate(equipment);

        StockParameterEntity entity = StockParameterEntity.builder()
                .equipment(equipment)
                .minStock(minStock)
                .securityStock(securityStock)
                .avgRestockTimeDays(request.getAvgRestockTimeDays())
                .avgStockConsumptionTimeDays(request.getAvgStockConsumptionTimeDays())
                .avgDefectiveRate(avgDefectiveRate)
                .build();

        StockParameterEntity saved = stockParameterRepository.save(entity);
        return MapperStockParameter.toResponseDTO(saved);
    }

    public List<StockParameterResponseDTO> findAll() {
        return stockParameterRepository.findAll()
                .stream()
                .map(MapperStockParameter::toResponseDTO)
                .collect(Collectors.toList());
    }

    public StockParameterResponseDTO findById(Long id) {
        StockParameterEntity entity = stockParameterRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(i18n.getMessage("stock.parameter.not.found")));
        return MapperStockParameter.toResponseDTO(entity);
    }

    public StockParameterResponseDTO update(Long id, StockParameterRequestDTO request) {
        StockParameterEntity existing = stockParameterRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(i18n.getMessage("stock.parameter.not.found")));

        EquipmentEntity equipment = equipmentRepository.findById(request.getEquipmentId())
                .orElseThrow(() -> new NotFoundException(i18n.getMessage("equipment.not.found", request.getEquipmentId())));

        int minStock = equipment.getType().getMinimumStock();
        int securityStock = equipment.getType().getSecurityStock();
        float avgDefectiveRate = calculateAvgDefectiveRate(equipment);

        existing.setEquipment(equipment);
        existing.setMinStock(minStock);
        existing.setSecurityStock(securityStock);
        existing.setAvgRestockTimeDays(request.getAvgRestockTimeDays());
        existing.setAvgStockConsumptionTimeDays(request.getAvgStockConsumptionTimeDays());
        existing.setAvgDefectiveRate(avgDefectiveRate);

        StockParameterEntity updated = stockParameterRepository.save(existing);
        return MapperStockParameter.toResponseDTO(updated);
    }

    public void delete(Long id) {
        StockParameterEntity existing = stockParameterRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(i18n.getMessage("stock.parameter.not.found")));
        stockParameterRepository.delete(existing);
    }
}

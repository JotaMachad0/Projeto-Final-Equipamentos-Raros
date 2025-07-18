package br.com.raroacademy.demo.service;

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

    public StockParameterResponseDTO create(StockParameterRequestDTO request) {
        EquipmentEntity equipment = equipmentRepository.findById(request.getEquipmentId())
                .orElseThrow(() -> new NotFoundException("Equipment not found with ID: " + request.getEquipmentId()));

        int minStock = equipment.getType().getMinimumStock();

        StockParameterEntity entity = StockParameterEntity.builder()
                .equipment(equipment)
                .minStock(minStock)
                .avgRestockTimeDays(request.getAvgRestockTimeDays())
                .avgStockConsumptionTimeDays(request.getAvgStockConsumptionTimeDays())
                .avgDefectiveRate(request.getAvgDefectiveRate())
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
                .orElseThrow(() -> new NotFoundException("Stock parameter not found with ID: " + id));
        return MapperStockParameter.toResponseDTO(entity);
    }

    public StockParameterResponseDTO update(Long id, StockParameterRequestDTO request) {
        StockParameterEntity existing = stockParameterRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Stock parameter not found with ID: " + id));

        EquipmentEntity equipment = equipmentRepository.findById(request.getEquipmentId())
                .orElseThrow(() -> new NotFoundException("Equipment not found with ID: " + request.getEquipmentId()));

        int minStock = equipment.getType().getMinimumStock();

        existing.setEquipment(equipment);
        existing.setMinStock(minStock);
        existing.setAvgRestockTimeDays(request.getAvgRestockTimeDays());
        existing.setAvgStockConsumptionTimeDays(request.getAvgStockConsumptionTimeDays());
        existing.setAvgDefectiveRate(request.getAvgDefectiveRate());

        StockParameterEntity updated = stockParameterRepository.save(existing);
        return MapperStockParameter.toResponseDTO(updated);
    }

    public void delete(Long id) {
        StockParameterEntity existing = stockParameterRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Stock parameter not found with ID: " + id));
        stockParameterRepository.delete(existing);
    }
}


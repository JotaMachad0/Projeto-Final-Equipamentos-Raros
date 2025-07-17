package br.com.raroacademy.demo.service;

import br.com.raroacademy.demo.domain.DTO.stock.parameters.StockParameterRequestDTO;
import br.com.raroacademy.demo.domain.DTO.stock.parameters.StockParameterResponseDTO;
import br.com.raroacademy.demo.domain.DTO.stock.parameters.MapperStockParameter;
import br.com.raroacademy.demo.domain.entities.StockParameterEntity;
import br.com.raroacademy.demo.domain.entities.EquipmentEntity;
import br.com.raroacademy.demo.exception.NotFoundException;
import br.com.raroacademy.demo.repository.StockParameterRepository;
import br.com.raroacademy.demo.repository.EquipmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockParameterService {

    private final StockParameterRepository repository;
    private final EquipmentRepository equipmentRepository;

    private static final int DEFAULT_DELIVERY_DAYS = 3; // SEDEX médio nacional

    public StockParameterResponseDTO create(StockParameterRequestDTO request) {
        EquipmentEntity equipment = equipmentRepository.findById(request.getEquipmentId())
                .orElseThrow(() -> new NotFoundException("Equipment not found with id: " + request.getEquipmentId()));

        // Define valor padrão para avgDeliveryTimeDays
        StockParameterEntity entity = MapperStockParameter.toEntity(request, equipment);
        entity.setAvgDeliveryTimeDays(DEFAULT_DELIVERY_DAYS);

        StockParameterEntity saved = repository.save(entity);
        return MapperStockParameter.toResponseDTO(saved);
    }

    public List<StockParameterResponseDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(MapperStockParameter::toResponseDTO)
                .collect(Collectors.toList());
    }

    public StockParameterResponseDTO findById(Long id) {
        StockParameterEntity entity = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("StockParameter not found with id: " + id));
        return MapperStockParameter.toResponseDTO(entity);
    }

    public StockParameterResponseDTO update(Long id, StockParameterRequestDTO request) {
        StockParameterEntity existing = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("StockParameter not found with id: " + id));

        EquipmentEntity equipment = equipmentRepository.findById(request.getEquipmentId())
                .orElseThrow(() -> new NotFoundException("Equipment not found with id: " + request.getEquipmentId()));

        existing.setEquipment(equipment);
        existing.setMinStock(request.getMinStock());
        existing.setAvgRestockTimeDays(request.getAvgRestockTimeDays());
        existing.setAvgStockConsumptionTimeDays(request.getAvgStockConsumptionTimeDays());
        existing.setAvgDefectiveRate(request.getAvgDefectiveRate());
        existing.setAvgDeliveryTimeDays(DEFAULT_DELIVERY_DAYS); // Mantém valor padrão

        StockParameterEntity updated = repository.save(existing);
        return MapperStockParameter.toResponseDTO(updated);
    }

    public void delete(Long id) {
        StockParameterEntity existing = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("StockParameter not found with id: " + id));
        repository.delete(existing);
    }
}

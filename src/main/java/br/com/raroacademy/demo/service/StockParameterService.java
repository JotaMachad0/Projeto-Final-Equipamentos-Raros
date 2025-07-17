package br.com.raroacademy.demo.service;

import br.com.raroacademy.demo.domain.DTO.stock.parameters.StockParameterRequestDTO;
import br.com.raroacademy.demo.domain.DTO.stock.parameters.StockParameterResponseDTO;
import br.com.raroacademy.demo.domain.entities.StockParameter;
import br.com.raroacademy.demo.domain.DTO.stock.parameters.MapperStockParameter;
import br.com.raroacademy.demo.exception.NotFoundException;
import br.com.raroacademy.demo.repository.StockParameterRepository;
import br.com.raroacademy.demo.service.CollaboratorService;
import br.com.raroacademy.demo.utils.RegionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockParameterService {

    private final StockParameterRepository repository;
    private final CollaboratorService collaboratorService;

    public StockParameterResponseDTO create(StockParameterRequestDTO request) {
        var entity = MapperStockParameter.toEntity(request);
        return MapperStockParameter.toResponseDTO(repository.save(entity));
    }

    public List<StockParameterResponseDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(MapperStockParameter::toResponseDTO)
                .collect(Collectors.toList());
    }

    public StockParameterResponseDTO findById(Long id) {
        var entity = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("StockParameter not found with id: " + id));
        return MapperStockParameter.toResponseDTO(entity);
    }

    public StockParameterResponseDTO update(Long id, StockParameterRequestDTO request) {
        var existing = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("StockParameter not found with id: " + id));

        existing.setEquipmentType(request.getEquipmentType());
        existing.setMinStock(request.getMinStock());
        existing.setAvgRestockTimeDays(request.getAvgRestockTimeDays());
        existing.setAvgStockConsumptionTimeDays(request.getAvgStockConsumptionTimeDays());
        existing.setAvgDefectiveRate(request.getAvgDefectiveRate());

        var collaborator = collaboratorService.getAnyCollaboratorWithValidCep()
                .orElseThrow(() -> new NotFoundException("No collaborator with valid CEP found"));

        String cep = collaborator.getAddress().getCep();
        String region = RegionUtils.getRegionFromCep(cep);
        int deliveryTime = RegionUtils.getDeliveryTimeByRegion(region);

        existing.setAvgDeliveryTimeDays(deliveryTime);

        return MapperStockParameter.toResponseDTO(repository.save(existing));
    }

    public void delete(Long id) {
        var existing = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("StockParameter not found with id: " + id));
        repository.delete(existing);
    }
}

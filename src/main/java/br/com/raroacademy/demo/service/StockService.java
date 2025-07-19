package br.com.raroacademy.demo.service;

import br.com.raroacademy.demo.commons.i18n.I18nUtil;
import br.com.raroacademy.demo.domain.entities.EquipmentEntity;
import br.com.raroacademy.demo.domain.enums.EquipmentType;
import br.com.raroacademy.demo.repository.EquipmentRepository;
import br.com.raroacademy.demo.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;
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

//    public StockParameterResponseDTO create(StockParameterRequestDTO request) {
//        EquipmentEntity equipment = equipmentRepository.findById(request.getEquipmentId())
//                .orElseThrow(() -> new NotFoundException(i18n.getMessage("equipment.not.found", request.getEquipmentId())));
//
//        if (stockParameterRepository.existsByEquipmentId(request.getEquipmentId())) {
//            throw new IllegalStateException(i18n.getMessage("stock.parameter.already.exists", request.getEquipmentId()));
//        }
//
////        int minStock = equipment.getType().getMinimumStock();
////        int securityStock = equipment.getType().getSecurityStock();
//        float avgDefectiveRate = calculateAvgDefectiveRate(equipment);
//
//        StockEntity entity = StockEntity.builder()
//                .equipment(equipment)
////                .minStock(minStock)
////                .securityStock(securityStock)
//                .currentStock(0)
//                .avgRestockTimeDays(request.getAvgRestockTimeDays())
//                .avgStockConsumptionTimeDays(request.getAvgStockConsumptionTimeDays())
//                .avgDefectiveRate(avgDefectiveRate)
//                .build();
//
//        StockEntity saved = stockParameterRepository.save(entity);
//        return MapperStockParameter.toResponseDTO(saved);
//    }

//    public List<StockParameterResponseDTO> findAll() {
//        return stockParameterRepository.findAll()
//                .stream()
//                .map(MapperStockParameter::toResponseDTO)
//                .collect(Collectors.toList());
//    }
//
//    public StockParameterResponseDTO findById(Long id) {
//        StockEntity entity = stockParameterRepository.findById(id)
//                .orElseThrow(() -> new NotFoundException(i18n.getMessage("stock.parameter.not.found")));
//        return MapperStockParameter.toResponseDTO(entity);
//    }

//    public StockParameterResponseDTO update(Long id, StockParameterRequestDTO request) {
//        StockEntity existing = stockParameterRepository.findById(id)
//                .orElseThrow(() -> new NotFoundException(i18n.getMessage("stock.parameter.not.found")));
//
//        EquipmentEntity equipment = equipmentRepository.findById(request.getEquipmentId())
//                .orElseThrow(() -> new NotFoundException(i18n.getMessage("equipment.not.found", request.getEquipmentId())));
//
////        int minStock = equipment.getType().getMinimumStock();
////        int securityStock = equipment.getType().getSecurityStock();
//        float avgDefectiveRate = calculateAvgDefectiveRate(equipment);
//
//        existing.setEquipment(equipment);
////        existing.setMinStock(minStock);
////        existing.setSecurityStock(securityStock);
//        existing.setAvgRestockTimeDays(request.getAvgRestockTimeDays());
//        existing.setAvgStockConsumptionTimeDays(request.getAvgStockConsumptionTimeDays());
//        existing.setAvgDefectiveRate(avgDefectiveRate);
//
//
//        StockEntity updated = stockParameterRepository.save(existing);
//        return MapperStockParameter.toResponseDTO(updated);
//    }

    public void incrementStock(EquipmentType equipmentType){
        var stock = stockRepository.findByEquipmentType(equipmentType);
        stock.setCurrentStock(stock.getCurrentStock() + 1);
        stockRepository.save(stock);
    }

    public void decrementStock(EquipmentType equipmentType){
        var stock = stockRepository.findByEquipmentType(equipmentType);
        stock.setCurrentStock(stock.getCurrentStock() - 1);
        stockRepository.save(stock);
        if (stock.getCurrentStock() < 0) {
            throw new IllegalStateException(i18n.getMessage("stock.negative.value"));
        }
    }

//    public void delete(Long id) {
//        StockEntity existing = stockParameterRepository.findById(id)
//                .orElseThrow(() -> new NotFoundException(i18n.getMessage("stock.parameter.not.found")));
//        stockParameterRepository.delete(existing);
//    }
//
//
//    public void consumeStock(Long equipmentId) {
//        StockEntity stock = stockParameterRepository.findByEquipmentId(equipmentId)
//                .orElseThrow(() -> new NotFoundException(i18n.getMessage("stock.parameter.not.found")));
//
//        stock.decrementCurrentStock();
//        stockParameterRepository.save(stock);
//    }

//    public void addStock(Long equipmentId) {
//        StockEntity stock = stockRepository.findByEquipmentId(equipmentId)
//                .orElseThrow(() -> new NotFoundException(i18n.getMessage("stock.parameter.not.found")));
//
//        stock.incrementCurrentStock();
//        stockParameterRepository.save(stock);
//    }
}

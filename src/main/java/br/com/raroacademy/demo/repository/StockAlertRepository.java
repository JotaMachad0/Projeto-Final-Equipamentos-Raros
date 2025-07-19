package br.com.raroacademy.demo.repository;

import br.com.raroacademy.demo.domain.entities.EquipmentStockEntity;
import br.com.raroacademy.demo.domain.entities.StockAlertEntity;
import br.com.raroacademy.demo.domain.enums.StockAlertStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockAlertRepository extends JpaRepository<StockAlertEntity, Long> {
    boolean existsByEquipmentStockAndStockAlertStatusNot(EquipmentStockEntity stock, StockAlertStatus status);

    List<StockAlertEntity> findByStockAlertStatus(StockAlertStatus status);

    @Query("SELECT alert FROM StockAlertEntity alert " +
            "JOIN alert.equipmentStock stock " +
            "ORDER BY stock.equipmentType ASC")
    List<StockAlertEntity> findAllSortedByEquipmentTypeLabel();
}

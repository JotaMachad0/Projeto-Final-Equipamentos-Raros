package br.com.raroacademy.demo.repository;

import br.com.raroacademy.demo.domain.entities.EquipmentStockEntity;
import br.com.raroacademy.demo.domain.entities.StockAlertEntity;
import br.com.raroacademy.demo.domain.entities.StockAlertStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockAlertRepository extends JpaRepository<StockAlertEntity, Long> {
    boolean existsByEquipmentStockAndStockAlertStatusNot(EquipmentStockEntity stock, StockAlertStatus status);
}

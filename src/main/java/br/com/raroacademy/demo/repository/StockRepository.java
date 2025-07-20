package br.com.raroacademy.demo.repository;

import br.com.raroacademy.demo.domain.entities.StockEntity;
import br.com.raroacademy.demo.domain.enums.EquipmentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends JpaRepository<StockEntity, Long> {
    StockEntity findByEquipmentType(EquipmentType equipmentType);
}

package br.com.raroacademy.demo.repository;

import br.com.raroacademy.demo.domain.entities.EquipmentType;
import br.com.raroacademy.demo.domain.entities.EquipmentStockEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EquipmentStockRepository extends JpaRepository<EquipmentStockEntity, Long> {
    Optional<EquipmentStockEntity> findByEquipmentType(EquipmentType type);
}

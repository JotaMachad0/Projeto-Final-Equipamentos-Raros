package br.com.raroacademy.demo.repository;

import br.com.raroacademy.demo.domain.entities.StockParameterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StockParameterRepository extends JpaRepository<StockParameterEntity, Long> {

    boolean existsByEquipmentId(Long equipmentId);

    Optional<StockParameterEntity> findByEquipmentId(Long equipmentId);
}

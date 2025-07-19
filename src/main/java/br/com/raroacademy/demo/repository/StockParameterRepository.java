package br.com.raroacademy.demo.repository;

import br.com.raroacademy.demo.domain.entities.StockParameterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockParameterRepository extends JpaRepository<StockParameterEntity, Long> {

    boolean existsByEquipmentId(Long equipmentId);
}

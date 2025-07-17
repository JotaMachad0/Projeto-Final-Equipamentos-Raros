package br.com.raroacademy.demo.repository;

import br.com.raroacademy.demo.domain.entities.EquipmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EquipmentRepository extends JpaRepository<EquipmentEntity, Long> {
    boolean existsBySerialNumber(String serialNumber);
}
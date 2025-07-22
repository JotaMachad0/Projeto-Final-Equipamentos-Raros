package br.com.raroacademy.demo.repository;

import br.com.raroacademy.demo.domain.entities.EquipmentEntity;
import br.com.raroacademy.demo.domain.enums.EquipmentStatus;
import br.com.raroacademy.demo.domain.enums.EquipmentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EquipmentRepository extends JpaRepository<EquipmentEntity, Long> {

    boolean existsBySerialNumber(String serialNumber);

    long countByType(EquipmentType type);

    long countByTypeAndStatus(EquipmentType type, EquipmentStatus status);
}

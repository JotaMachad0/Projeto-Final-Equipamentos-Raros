package br.com.raroacademy.demo.repository;

import br.com.raroacademy.demo.domain.entities.EquipmentPurchasesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EquipmentPurchasesRepository extends JpaRepository<EquipmentPurchasesEntity, Long> {
}
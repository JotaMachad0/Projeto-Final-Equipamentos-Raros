package br.com.raroacademy.demo.repository;

import br.com.raroacademy.demo.domain.entities.EquipmentCollaboratorEntity;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface EquipmentCollaboratorRepository extends JpaRepository<EquipmentCollaboratorEntity, Long> {
    @Query("SELECT ec FROM EquipmentCollaboratorEntity ec " +
            "WHERE ec.equipment.id = :equipmentId " +
            "AND (ec.returnDate IS NULL OR ec.returnDate >= :deliveryDate)")
    List<EquipmentCollaboratorEntity> findUnavailableEquipments(
            @Param("equipmentId") Long equipmentId,
            @Param("deliveryDate") LocalDate deliveryDate);
}
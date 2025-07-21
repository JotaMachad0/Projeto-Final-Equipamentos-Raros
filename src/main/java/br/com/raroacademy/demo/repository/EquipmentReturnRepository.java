package br.com.raroacademy.demo.repository;

import br.com.raroacademy.demo.domain.entities.EquipmentReturnEntity;
import br.com.raroacademy.demo.domain.enums.ReturnStatus;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EquipmentReturnRepository extends JpaRepository<EquipmentReturnEntity, Long> {
    boolean existsByEquipmentCollaboratorId(@NotNull Long equipmentCollaboratorId);

    boolean existsByEquipmentCollaboratorIdAndStatus(@NotNull Long aLong, ReturnStatus returnStatus);
}
package br.com.raroacademy.demo.repository;

import br.com.raroacademy.demo.domain.entities.ExpectedReturnEntity;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpectedReturnRepository extends JpaRepository<ExpectedReturnEntity, Long> {
    boolean existsByEquipmentCollaboratorId(@NotNull Long equipmentCollaboratorId);
}
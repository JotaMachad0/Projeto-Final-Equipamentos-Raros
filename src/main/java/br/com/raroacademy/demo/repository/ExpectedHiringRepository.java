package br.com.raroacademy.demo.repository;

import br.com.raroacademy.demo.domain.entities.ExpectedHiringEntity;
import br.com.raroacademy.demo.domain.entities.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface ExpectedHiringRepository extends JpaRepository<ExpectedHiringEntity, Long> {
    boolean existsByExpectedHireDateAndPositionIgnoreCaseAndEquipmentRequirementsIgnoreCaseAndRegion(
            LocalDate expectedHireDate,
            String position,
            String equipmentRequirements,
            Region region
    );

    boolean existsByExpectedHireDateAndPositionIgnoreCaseAndEquipmentRequirementsIgnoreCaseAndRegionAndIdNot(
            LocalDate expectedHireDate,
            String position,
            String equipmentRequirements,
            Region region,
            Long id
    );
}

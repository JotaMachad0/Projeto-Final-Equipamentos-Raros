package br.com.raroacademy.demo.repository;

import br.com.raroacademy.demo.domain.entities.ExpectedHiringEntity;
import br.com.raroacademy.demo.domain.entities.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface ExpectedHiringRepository extends JpaRepository<ExpectedHiringEntity, Long> {
    Optional<ExpectedHiringEntity> findByExpectedHireDateAndPositionIgnoreCaseAndRegion(
            LocalDate expectedHireDate,
            String position,
            Region region
    );

    Optional<ExpectedHiringEntity> findByExpectedHireDateAndPositionIgnoreCaseAndRegionAndIdNot(
            LocalDate expectedHireDate,
            String position,
            Region region,
            Long id
    );
}

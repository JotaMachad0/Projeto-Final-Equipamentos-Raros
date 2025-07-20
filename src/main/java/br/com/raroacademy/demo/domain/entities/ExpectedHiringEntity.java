package br.com.raroacademy.demo.domain.entities;

import br.com.raroacademy.demo.domain.enums.ExpectedHiringStatus;
import br.com.raroacademy.demo.domain.enums.Region;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@EqualsAndHashCode
@Table(name = "expected_hirings")
public class ExpectedHiringEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "expected_hire_date", nullable = false)
    private LocalDate expectedHireDate;

    @Column(name = "position", nullable = false, length = 100)
    private String position;

    @Column(name = "equipment_requirements", nullable = false)
    private String equipmentRequirements;

    @Enumerated(EnumType.STRING)
    @Column(name = "region", nullable = false, length = 12)
    private Region region;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 10)
    private ExpectedHiringStatus expectedHiringStatus;

    @Builder
    public ExpectedHiringEntity(Long id, LocalDate expectedHireDate, String position, String equipmentRequirements,
                                Region region, ExpectedHiringStatus expectedHiringStatus) {
        this.id = id;
        this.expectedHireDate = expectedHireDate;
        this.position = position;
        this.equipmentRequirements = equipmentRequirements;
        this.region = region;
        this.expectedHiringStatus = expectedHiringStatus;
    }
}

package br.com.raroacademy.demo.domain.entities;

import br.com.raroacademy.demo.domain.enums.EquipmentStatus;
import br.com.raroacademy.demo.domain.enums.EquipmentType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "equipments")
public class EquipmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EquipmentType type;

    @Column(nullable = false, unique = true)
    private String serialNumber;

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    private String model;

    @Column(columnDefinition = "TEXT")
    private String specs;

    @Column(nullable = false)
    private LocalDate acquisitionDate;

    @Column(nullable = false)
    private Integer usageTimeMonths;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EquipmentStatus status;

    @Builder
    public EquipmentEntity(Long id, EquipmentType type, String serialNumber, String model, String brand,
                                String specs, LocalDate acquisitionDate, Integer usageTimeMonths, String status) {
        this.id = id;
        this.type = type;
        this.serialNumber = serialNumber;
        this.model = model;
        this.brand = brand;
        this.specs = specs;
        this.acquisitionDate = acquisitionDate;
        this.usageTimeMonths = usageTimeMonths;
        this.equipmentstatus = status;
    }

}

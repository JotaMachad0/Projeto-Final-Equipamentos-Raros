package br.com.raroacademy.demo.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "equipments")
public class EquipmentEntity {

    @Id
    private Long id;

    @Column(nullable = false)
    private String type;

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

    @Column(nullable = false)
    private String status;
}

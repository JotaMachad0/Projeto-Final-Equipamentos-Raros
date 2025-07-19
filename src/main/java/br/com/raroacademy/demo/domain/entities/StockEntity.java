package br.com.raroacademy.demo.domain.entities;

import br.com.raroacademy.demo.domain.enums.EquipmentType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "stock")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "equipment_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private EquipmentType equipmentType;

    @Column(name = "min_stock", nullable = false)
    private Integer minStock;

    @Column(name = "security_stock", nullable = false)
    private Integer securityStock;

    @Column(name = "current_stock", nullable = false)
    private Integer currentStock;

    @Column(name = "avg_restock_time_days", nullable = false)
    private Integer avgRestockTimeDays;

    @Column(name = "avg_stock_consumption_time_days", nullable = false)
    private Integer avgStockConsumptionTimeDays;

    @Column(name = "avg_defective_rate", nullable = false)
    private Float avgDefectiveRate;

}

package br.com.raroacademy.demo.domain.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "stock_parameters")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockParameterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "min_stock", nullable = false)
    private Integer minStock;

    @Column(name = "security_stock", nullable = false)
    private Integer securityStock;

    @Column(name = "avg_restock_time_days", nullable = false)
    private Integer avgRestockTimeDays;

    @Column(name = "avg_stock_consumption_time_days", nullable = false)
    private Integer avgStockConsumptionTimeDays;

    @Column(name = "avg_defective_rate", nullable = false)
    private Float avgDefectiveRate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipment_id", nullable = false)
    private EquipmentEntity equipment;
}

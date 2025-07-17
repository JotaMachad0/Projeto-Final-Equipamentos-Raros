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
public class StockParameter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "equipment_type", nullable = false)
    private String equipmentType;

    @Column(name = "min_stock", nullable = false)
    private Integer minStock;

    @Column(name = "avg_restock_time_days", nullable = false)
    private Integer avgRestockTimeDays;

    @Column(name = "avg_stock_consumption_time_days", nullable = false)
    private Integer avgStockConsumptionTimeDays;

    @Column(name = "avg_delivery_time_days", nullable = false)
    private Integer avgDeliveryTimeDays;

    @Column(name = "avg_defective_rate", nullable = false)
    private Float avgDefectiveRate;
}

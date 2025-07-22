package br.com.raroacademy.demo.domain.entities;

import br.com.raroacademy.demo.domain.enums.EquipmentType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

@Entity
@Table(name = "stock")
@Getter
@Setter
@EqualsAndHashCode
@RequiredArgsConstructor
public class StockEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "equipment_type", nullable = false, unique = true, length = 21)
    private EquipmentType equipmentType;

    @Min(value = 0, message = "Current stock can't be negative.")
    @Column(name = "current_stock", nullable = false)
    private Integer currentStock;

    @Min(value = 0, message = "Minimum stock can't be negative.")
    @Column(name = "min_stock")
    private Integer minStock;

    @Min(value = 0, message = "Security stock can't be negative.")
    @Column(name = "security_stock")
    private Integer securityStock;

    @Min(value = 0, message = "Time can't be negative.")
    @Column(name = "avg_restock_time_days")
    private Integer avgRestockTimeDays;

    @Min(value = 0, message = "Time can't be negative.")
    @Column(name = "avg_stock_consumption_time_days")
    private Integer avgStockConsumptionTimeDays;

    @Min(value = 0, message = "Rates can't be negative.")
    @Column(name = "avg_defective_rate")
    private Float avgDefectiveRate;

    @Builder
    public StockEntity(Long id, EquipmentType equipmentType,
                       Integer currentStock, Integer minStock,
                       Integer securityStock, Integer avgRestockTimeDays,
                       Integer avgStockConsumptionTimeDays, Float avgDefectiveRate) {
        this.id = id;
        this.equipmentType = equipmentType;
        this.currentStock = currentStock;
        this.minStock = minStock;
        this.securityStock= securityStock;
        this.avgRestockTimeDays = avgRestockTimeDays;
        this.avgStockConsumptionTimeDays = avgStockConsumptionTimeDays;
        this.avgDefectiveRate = avgDefectiveRate;
    }
}

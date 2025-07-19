package br.com.raroacademy.demo.domain.entities;

import br.com.raroacademy.demo.domain.enums.StockAlertStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "stock_alerts")
public class StockAlertEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "{equipment.stock.required}")
    @ManyToOne(optional = false)
    @JoinColumn(name = "equipment_stock_id", nullable = false)
    private EquipmentStockEntity equipmentStock;

    @NotNull(message = "{minimum.stock.required}")
    @Min(value = 0, message = "{minimum.stock.negative}")
    @Column(name = "minimum_stock", nullable = false)
    private Integer minimumStock;

    @NotNull(message = "{security.stock.required}")
    @Min(value = 0, message = "{security.stock.negative}")
    @Column(name = "security_stock", nullable = false)
    private Integer securityStock;

    @NotNull
    @Column(name = "alert_sent_at", nullable = false)
    private Timestamp alertSentAt;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "stock_alert_status", nullable = false)
    private StockAlertStatus stockAlertStatus;

    @Builder
    public StockAlertEntity(Long id, EquipmentStockEntity equipmentStock, Integer minimumStock,
                            Integer securityStock, Timestamp alertSentAt, StockAlertStatus stockAlertStatus) {
        this.id = id;
        this.equipmentStock = equipmentStock;
        this.minimumStock = minimumStock;
        this.securityStock = securityStock;
        this.alertSentAt = alertSentAt;
        this.stockAlertStatus = stockAlertStatus;
    }
}

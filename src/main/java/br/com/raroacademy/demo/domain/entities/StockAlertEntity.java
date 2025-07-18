package br.com.raroacademy.demo.domain.entities;

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

    @NotNull(message = "O estoque de equipamento é obrigatório")
    @ManyToOne(optional = false)
    @JoinColumn(name = "equipment_stock_id", nullable = false)
    private EquipmentStockEntity equipmentStock;

    @NotNull(message = "O estoque mínimo é obrigatório")
    @Min(value = 0, message = "O estoque mínimo não pode ser negativo")
    @Column(name = "minimum_stock", nullable = false)
    private Integer minimumStock;

    @NotNull(message = "O estoque de segurança é obrigatório")
    @Min(value = 0, message = "O estoque de segurança não pode ser negativo")
    @Column(name = "security_stock", nullable = false)
    private Integer securityStock;

    @NotNull(message = "A data de envio do alerta é obrigatória")
    @Column(name = "alert_sent_at", nullable = false)
    private Timestamp alertSentAt;

    @NotNull(message = "O status do alerta é obrigatório")
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

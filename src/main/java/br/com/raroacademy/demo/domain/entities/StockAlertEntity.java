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

    @NotNull(message = "{stock.required}")
    @ManyToOne(optional = false)
    @JoinColumn(name = "stock_id", nullable = false)
    private StockEntity stock;

//    @NotNull(message = "{minimum.stock.required}")
//    @Min(value = 0, message = "{minimum.stock.negative}")
//    @Column(name = "minimum_stock", nullable = false)
//    private Integer minimumStock;
//
//    @NotNull(message = "{security.stock.required}")
//    @Min(value = 0, message = "{security.stock.negative}")
//    @Column(name = "security_stock", nullable = false)
//    private Integer securityStock;

    @NotNull
    @Column(name = "alert_sent_at", nullable = false)
    private Timestamp alertSentAt;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "stock_alert_status", nullable = false, length = 10)
    private StockAlertStatus stockAlertStatus;

    @Builder
    public StockAlertEntity(Long id, StockEntity stock, Timestamp alertSentAt, StockAlertStatus stockAlertStatus) {
        this.id = id;
        this.stock = stock;
//        this.minimumStock = minimumStock;
//        this.securityStock = securityStock;
        this.alertSentAt = alertSentAt;
        this.stockAlertStatus = stockAlertStatus;
    }
}

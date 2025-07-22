package br.com.raroacademy.demo.domain.entities;

import br.com.raroacademy.demo.domain.enums.EquipmentType;
import br.com.raroacademy.demo.domain.enums.PurchaseStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "equipment_purchases")
public class EquipmentPurchasesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "equipment_type", nullable = false, length = 100)
    private EquipmentType equipmentType;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "order_date", nullable = false)
    private LocalDate orderDate;

    @Column(name = "receipt_date")
    private LocalDate receiptDate;

    @Column(name = "supplier", nullable = false, length = 100)
    private String supplier;
    
    @Column(name = "status", length = 20)
    @Enumerated(EnumType.STRING)
    private PurchaseStatus status;

    @Builder
    public EquipmentPurchasesEntity(Long id, EquipmentType equipmentType, Integer quantity, LocalDate orderDate,
                                    LocalDate receiptDate, String supplier, PurchaseStatus status) {
        this.id = id;
        this.equipmentType = equipmentType;
        this.quantity = quantity;
        this.orderDate = orderDate;
        this.receiptDate = receiptDate;
        this.supplier = supplier;
        this.status = status;
    }
}
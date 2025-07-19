package br.com.raroacademy.demo.domain.entities;

import br.com.raroacademy.demo.domain.enums.EquipmentType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "equipments_stock")
public class EquipmentStockEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "{equipment.type.required}")
    @Enumerated(EnumType.STRING)
    @Column(name = "equipment_type", nullable = false, unique = true)
    private EquipmentType equipmentType;

    @NotNull(message = "{current.stock.required}")
    @Min(value = 0, message = "{current.stock.negative}")
    @Column(name = "current_stock", nullable = false)
    private Integer currentStock;

    @Builder
    public EquipmentStockEntity(Long id, EquipmentType equipmentType, Integer currentStock) {
        this.id = id;
        this.equipmentType = equipmentType;
        this.currentStock = currentStock;
    }
}

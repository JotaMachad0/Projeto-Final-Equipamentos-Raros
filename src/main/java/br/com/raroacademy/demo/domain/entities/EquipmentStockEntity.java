package br.com.raroacademy.demo.domain.entities;

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

    @NotNull(message = "O tipo de equipamento é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(name = "equipment_type", nullable = false, unique = true)
    private EquipmentType equipmentType;

    @NotNull(message = "A quantidade é obrigatória")
    @Min(value = 0, message = "A quantidade não pode ser negativa")
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Builder
    public EquipmentStockEntity(Long id, EquipmentType equipmentType, Integer quantity) {
        this.id = id;
        this.equipmentType = equipmentType;
        this.quantity = quantity;
    }
}

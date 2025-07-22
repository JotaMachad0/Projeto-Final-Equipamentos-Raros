package br.com.raroacademy.demo.domain.entities;

import br.com.raroacademy.demo.domain.enums.ReturnStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "equipment_return")
public class EquipmentReturnEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "delivery_date", nullable = false)
    private LocalDate deliveryDate;

    @Column(name = "receipt_date")
    private LocalDate receiptDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ReturnStatus status;

    @Column(name = "note")
    private String note;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipment_collaborator_id", nullable = false, unique = true)
    private EquipmentCollaboratorEntity equipmentCollaborator;
}
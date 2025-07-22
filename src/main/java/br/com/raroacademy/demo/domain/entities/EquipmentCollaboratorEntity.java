package br.com.raroacademy.demo.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "equipment_collaborator")
@NoArgsConstructor
public class EquipmentCollaboratorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "collaborator_id", nullable = false)
    private CollaboratorEntity collaborator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipment_id", nullable = false)
    private EquipmentEntity equipment;

    @Column(name = "delivery_date", nullable = false)
    private LocalDate deliveryDate;

    @Column(name = "prevision_delivery_date")
    private LocalDate previsionDeliveryDate;

    @Column(name = "return_date")
    private LocalDate returnDate;

    @Column(name = "delivery_status")
    private String deliveryStatus;

    @Column(name = "notes")
    private String notes;

    @Builder
    public EquipmentCollaboratorEntity(Long id, CollaboratorEntity collaborator, EquipmentEntity equipment, LocalDate deliveryDate,
                                       LocalDate previsionDeliveryDate, LocalDate returnDate, String deliveryStatus, String notes) {
        this.id = id;
        this.collaborator = collaborator;
        this.equipment = equipment;
        this.deliveryDate = deliveryDate;
        this.previsionDeliveryDate = previsionDeliveryDate;
        this.returnDate = returnDate;
        this.deliveryStatus = deliveryStatus;
        this.notes = notes;
    }
}
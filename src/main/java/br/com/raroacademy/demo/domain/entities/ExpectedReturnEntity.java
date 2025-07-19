package br.com.raroacademy.demo.domain.entities;

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
@Table(name = "retornos_previstos")
public class ExpectedReturnEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "expected_return_date", nullable = false)
    private LocalDate expectedReturnDate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipment_collaborator_id", nullable = false, unique = true)
    private EquipmentCollaboratorEntity equipmentCollaborator;

    @Builder
    public ExpectedReturnEntity(Long id, LocalDate expectedReturnDate, EquipmentCollaboratorEntity equipmentCollaborator) {
        this.id = id;
        this.expectedReturnDate = expectedReturnDate;
        this.equipmentCollaborator = equipmentCollaborator;
    }
}
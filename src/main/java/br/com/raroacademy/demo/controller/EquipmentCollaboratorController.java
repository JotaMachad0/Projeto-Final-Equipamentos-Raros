package br.com.raroacademy.demo.controller;

import br.com.raroacademy.demo.commons.annotations.ApiController;
import br.com.raroacademy.demo.commons.annotations.OpenApiController;
import br.com.raroacademy.demo.domain.DTO.equipment.collaborator.EquipmentCollaboratorRequestDTO;
import br.com.raroacademy.demo.domain.DTO.equipment.collaborator.EquipmentCollaboratorResponseDTO;
import br.com.raroacademy.demo.domain.DTO.equipment.collaborator.NewCollaboratorEquipmentLinkRequestDTO;
import br.com.raroacademy.demo.domain.annotations.equipment.collaborator.*;
import br.com.raroacademy.demo.service.EquipmentCollaboratorService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@OpenApiController(name = "Equipments-Collaborators")
@ApiController(path = "/equipments-collaborators")
@AllArgsConstructor
public class EquipmentCollaboratorController {

    private final EquipmentCollaboratorService equipmentCollaboratorService;

    @CreateEquipmentCollaboratorEndpoint
    public ResponseEntity<EquipmentCollaboratorResponseDTO> create(@RequestBody @Valid NewCollaboratorEquipmentLinkRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(equipmentCollaboratorService.create(dto));
    }

    @GetEquipmentCollaboratorEndpoint
    public ResponseEntity<EquipmentCollaboratorResponseDTO> getById(@PathVariable Long id) {
        var response = equipmentCollaboratorService.getById(id);
        return ResponseEntity.ok(response);
    }

    @GetAllEquipmentCollaboratorEndpoint
    public ResponseEntity<List<EquipmentCollaboratorResponseDTO>> getAll() {
        return ResponseEntity.ok(equipmentCollaboratorService.getAll());
    }

    @UpdateEquipmentCollaboratorEndpoint
    public ResponseEntity<EquipmentCollaboratorResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody EquipmentCollaboratorRequestDTO request) {
        var response = equipmentCollaboratorService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteEquipmentCollaboratorEndpoint
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        equipmentCollaboratorService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
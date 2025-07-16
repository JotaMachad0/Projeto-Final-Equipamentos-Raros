package br.com.raroacademy.demo.controller;

import br.com.raroacademy.demo.commons.annotations.ApiController;
import br.com.raroacademy.demo.commons.annotations.OpenApiController;
import br.com.raroacademy.demo.domain.DTO.equipment.EquipmentRequestDTO;
import br.com.raroacademy.demo.domain.DTO.equipment.EquipmentResponseDTO;
import br.com.raroacademy.demo.domain.annotations.equipment.*;
import br.com.raroacademy.demo.service.EquipmentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@OpenApiController(name = "Equipments")
@ApiController(path = "/equipments")
@AllArgsConstructor
public class EquipmentController {

    private final EquipmentService equipmentService;


    @CreateEquipmentEndpoint
    public ResponseEntity<EquipmentResponseDTO> create(@RequestBody @Valid EquipmentRequestDTO dto) {
        return ResponseEntity.ok(equipmentService.create(dto));
    }

    @GetEquipmentEndpoint
    public ResponseEntity<EquipmentResponseDTO> getEquipment(@PathVariable Long id) {
        var equipment = equipmentService.getEquipmentById(id);
        return ResponseEntity.ok(equipment);
    }

    @GetAllEquipmentsEndpoint
    public ResponseEntity<List<EquipmentResponseDTO>> getAll() {
        return ResponseEntity.ok(equipmentService.getAll());
    }

    @UpdateEquipmentEndpoint
    public ResponseEntity<EquipmentResponseDTO> updateEquipment(
            @PathVariable Long id,
            @Valid @RequestBody EquipmentRequestDTO request
    ) {
        var response = equipmentService.update(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteEquipmentEndpoint
    public ResponseEntity<Void> deleteEquipment(@PathVariable Long id) {
        equipmentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

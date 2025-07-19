package br.com.raroacademy.demo.controller;

import br.com.raroacademy.demo.commons.annotations.ApiController;
import br.com.raroacademy.demo.commons.annotations.OpenApiController;
import br.com.raroacademy.demo.domain.DTO.stock.UpdateStockRequestDTO;
import br.com.raroacademy.demo.domain.enums.EquipmentType;
import br.com.raroacademy.demo.service.StockService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@OpenApiController(name = "Stock")
@ApiController(path = "/stock")
@AllArgsConstructor
public class StockController {

    private final StockService service;

    @PutMapping("/{equipmentType}")
    public ResponseEntity<?> updateStockByType(
            @PathVariable String equipmentType,
            @Valid @RequestBody UpdateStockRequestDTO request
    ) {
        try {
            EquipmentType type = EquipmentType.fromLabel(equipmentType);
            var response = service.updateByEquipmentType(type, request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Tipo de equipamento inválido: " + equipmentType);
        }
    }

    @GetMapping("/{equipmentType}")
    public ResponseEntity<?> getStockByType(@PathVariable String equipmentType) {
        try {
            EquipmentType type = EquipmentType.fromLabel(equipmentType);
            var response = service.findByEquipmentType(type);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Tipo de equipamento inválido: " + equipmentType);
        }
    }
}

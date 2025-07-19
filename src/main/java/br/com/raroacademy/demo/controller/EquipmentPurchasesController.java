package br.com.raroacademy.demo.controller;

import br.com.raroacademy.demo.commons.annotations.ApiController;
import br.com.raroacademy.demo.commons.annotations.OpenApiController;

import br.com.raroacademy.demo.domain.DTO.equipmentPurchase.EquipmentPurchasesRequestDTO;
import br.com.raroacademy.demo.domain.DTO.equipmentPurchase.EquipmentPurchasesResponseDTO;
import br.com.raroacademy.demo.domain.annotations.equipment_purchase.*;
import br.com.raroacademy.demo.service.EquipmentPurchasesService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@OpenApiController(name = "Equipment Purchases")
@ApiController(path = "/equipment-purchases")
@AllArgsConstructor
public class EquipmentPurchasesController {

    private final EquipmentPurchasesService equipmentPurchasesService;

    @CreatePurchaseEndpoint
    public ResponseEntity<EquipmentPurchasesResponseDTO> createPurchase(@Valid @RequestBody EquipmentPurchasesRequestDTO request) {
        var response = equipmentPurchasesService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetPurchaseEndpoint
    public ResponseEntity<EquipmentPurchasesResponseDTO> getPurchaseById(@PathVariable Long id) {
        var purchase = equipmentPurchasesService.getById(id);
        return ResponseEntity.ok(purchase);
    }

    @GetAllPurchasesEndpoint
    public ResponseEntity<List<EquipmentPurchasesResponseDTO>> getAllPurchases() {
        var purchases = equipmentPurchasesService.getAll();
        return ResponseEntity.ok(purchases);
    }

    @UpdatePurchaseEndpoint
    public ResponseEntity<EquipmentPurchasesResponseDTO> updatePurchase(
            @PathVariable Long id,
            @Valid @RequestBody EquipmentPurchasesRequestDTO request
    ) {
        var response = equipmentPurchasesService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @RegisterInStockEndpoint
    public ResponseEntity<EquipmentPurchasesResponseDTO> registerInStock(@PathVariable Long id) {
        var response = equipmentPurchasesService.registerInStock(id);
        return ResponseEntity.ok(response);
    }
}

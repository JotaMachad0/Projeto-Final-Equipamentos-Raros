package br.com.raroacademy.demo.controller;

import br.com.raroacademy.demo.commons.annotations.ApiController;
import br.com.raroacademy.demo.commons.annotations.OpenApiController;
import br.com.raroacademy.demo.commons.i18n.I18nUtil;
import br.com.raroacademy.demo.domain.DTO.stock.EquipmentStockRequestDTO;
import br.com.raroacademy.demo.domain.DTO.stock.EquipmentStockResponseDTO;
import br.com.raroacademy.demo.domain.annotations.stock.*;
import br.com.raroacademy.demo.service.EquipmentStockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@OpenApiController(name = "Equipment stock")
@ApiController(path = "/equipment-stock")
@RequiredArgsConstructor
public class EquipmentStockController {

    private final EquipmentStockService equipmentStockService;
    private final I18nUtil i18nUtil;

    @CreateEquipmentStockEndpoint
    public ResponseEntity<EquipmentStockResponseDTO> createEquipmentStock(
            @RequestBody @Valid EquipmentStockRequestDTO request) {
        var response = equipmentStockService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @UpdateEquipmentStockEndpoint
    public ResponseEntity<EquipmentStockResponseDTO> updateEquipmentStock(
            @PathVariable Long id,
            @RequestBody @Valid EquipmentStockRequestDTO request) {
        var response = equipmentStockService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @GetEquipmentStockEndpoint
    public ResponseEntity<EquipmentStockResponseDTO> getEquipmentStock(@PathVariable Long id) {
        var response = equipmentStockService.getEquipmentStockById(id);
        return ResponseEntity.ok(response);
    }

    @GetAllEquipmentStocksEndpoint
    public ResponseEntity<List<EquipmentStockResponseDTO>> getAllEquipmentStock() {
        var responseList = equipmentStockService.getAllEquipmentStocks();
        return ResponseEntity.ok(responseList);
    }

    @DeleteEquipmentStockEndpoint
    public ResponseEntity<String> deleteEquipmentStock(@PathVariable Long id) {
        equipmentStockService.delete(id);
        return ResponseEntity.ok(i18nUtil.getMessage("equipment.stock.deleted"));
    }
}

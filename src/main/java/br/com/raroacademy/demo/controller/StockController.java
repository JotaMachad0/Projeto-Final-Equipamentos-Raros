package br.com.raroacademy.demo.controller;

import br.com.raroacademy.demo.commons.annotations.ApiController;
import br.com.raroacademy.demo.commons.annotations.OpenApiController;
import br.com.raroacademy.demo.commons.i18n.I18nUtil;
import br.com.raroacademy.demo.domain.DTO.stock.StockRequestDTO;
import br.com.raroacademy.demo.domain.DTO.stock.StockResponseDTO;
import br.com.raroacademy.demo.domain.annotations.stock.*;
import br.com.raroacademy.demo.domain.enums.EquipmentType;
import br.com.raroacademy.demo.service.StockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@OpenApiController(name = "Stock")
@ApiController(path = "/stock")
public class StockController {

    private final StockService stockService;
    private final I18nUtil i18n;

    @GetAllStocksEndpoint
    public ResponseEntity<List<StockResponseDTO>> getAll() {
        log.info("Buscando todos os estoques");
        return ResponseEntity.ok(stockService.getAllStocks());
    }

    @GetStockByEquipmentTypeEndpoint
    public ResponseEntity<StockResponseDTO> getByType(@PathVariable EquipmentType equipmentType) {
        log.info("Buscando estoque do tipo: {}", equipmentType);
        return ResponseEntity.ok(stockService.findByEquipmentType(equipmentType));
    }

    @UpdateStockByEquipmentTypeEndpoint
    public ResponseEntity<StockResponseDTO> update(
            @PathVariable("equipmentType") EquipmentType equipmentType,
            @Valid @RequestBody StockRequestDTO request
    ) {
        log.info("Atualizando estoque do tipo: {}", equipmentType);
        return ResponseEntity.ok(stockService.updateByEquipmentType(equipmentType, request));
    }
}
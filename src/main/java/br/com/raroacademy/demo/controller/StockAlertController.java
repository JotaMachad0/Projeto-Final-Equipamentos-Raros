package br.com.raroacademy.demo.controller;

import br.com.raroacademy.demo.commons.annotations.ApiController;
import br.com.raroacademy.demo.commons.annotations.OpenApiController;
import br.com.raroacademy.demo.commons.i18n.I18nUtil;
import br.com.raroacademy.demo.domain.DTO.stock.allert.StockAlertResponseDTO;
import br.com.raroacademy.demo.domain.annotations.stock.allert.GetAllStockAlertsEndpoint;
import br.com.raroacademy.demo.domain.annotations.stock.allert.GetStockAlertEndpoint;
import br.com.raroacademy.demo.domain.annotations.stock.allert.ProcessStockAlertEndpoint;
import br.com.raroacademy.demo.service.StockAlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@OpenApiController(name = "Stock alerts")
@ApiController(path = "/stock-alerts")
@RequiredArgsConstructor
public class StockAlertController {

    private final StockAlertService stockAlertService;
    private final I18nUtil i18nUtil;

    @GetAllStockAlertsEndpoint
    public ResponseEntity<List<StockAlertResponseDTO>> getAllAlerts() {
        var alerts = stockAlertService.getAllStockAlerts();
        return ResponseEntity.ok(alerts);
    }

    @GetStockAlertEndpoint
    public ResponseEntity<StockAlertResponseDTO> getAlert(@PathVariable Long id) {
        var alert = stockAlertService.getStockAlertById(id);
        return ResponseEntity.ok(alert);
    }

    @ProcessStockAlertEndpoint
    public ResponseEntity<String> processStockAlert(@PathVariable Long id) {
        stockAlertService.markAsProcessed(id);
        return ResponseEntity.ok(i18nUtil.getMessage("stock.alert.status.update"));
    }
}

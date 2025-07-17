package br.com.raroacademy.demo.controller;

import br.com.raroacademy.demo.commons.annotations.ApiController;
import br.com.raroacademy.demo.commons.annotations.OpenApiController;
import br.com.raroacademy.demo.domain.DTO.stock.parameters.StockParameterRequestDTO;
import br.com.raroacademy.demo.domain.DTO.stock.parameters.StockParameterResponseDTO;
import br.com.raroacademy.demo.domain.annotations.stock.parameter.CreateStockParameterEndpoint;
import br.com.raroacademy.demo.domain.annotations.stock.parameter.GetAllStockParametersEndpoint;
import br.com.raroacademy.demo.domain.annotations.stock.parameter.GetStockParameterByIdEndpoint;
import br.com.raroacademy.demo.domain.annotations.stock.parameter.UpdateStockParameterEndpoint;
import br.com.raroacademy.demo.domain.annotations.stock.parameter.DeleteStockParameterEndpoint;
import br.com.raroacademy.demo.service.StockParameterService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@OpenApiController(name = "Stock Parameters")
@ApiController(path = "/stock-parameters")
@AllArgsConstructor
public class StockParameterController {

    private final StockParameterService service;

    @CreateStockParameterEndpoint
    public ResponseEntity<StockParameterResponseDTO> createStockParameter(
            @Valid @RequestBody StockParameterRequestDTO request
    ) {
        var response = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetAllStockParametersEndpoint
    public ResponseEntity<List<StockParameterResponseDTO>> getAllStockParameters() {
        var list = service.findAll();
        return ResponseEntity.ok(list);
    }

    @GetStockParameterByIdEndpoint
    public ResponseEntity<StockParameterResponseDTO> getById(@PathVariable Long id) {
        var response = service.findById(id);
        return ResponseEntity.ok(response);
    }

    @UpdateStockParameterEndpoint
    public ResponseEntity<StockParameterResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody StockParameterRequestDTO request
    ) {
        var response = service.update(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteStockParameterEndpoint
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

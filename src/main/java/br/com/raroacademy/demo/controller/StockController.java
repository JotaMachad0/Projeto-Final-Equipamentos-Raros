package br.com.raroacademy.demo.controller;

import br.com.raroacademy.demo.commons.annotations.ApiController;
import br.com.raroacademy.demo.commons.annotations.OpenApiController;
import br.com.raroacademy.demo.service.StockService;
import lombok.AllArgsConstructor;

@OpenApiController(name = "Stock Parameters")
@ApiController(path = "/stock-parameters")
@AllArgsConstructor
public class StockController {

    private final StockService service;

//    @CreateStockParameterEndpoint
//    public ResponseEntity<StockParameterResponseDTO> createStockParameter(
//            @Valid @RequestBody StockParameterRequestDTO request
//    ) {
//        var response = service.create(request);
//        return ResponseEntity.status(HttpStatus.CREATED).body(response);
//    }
//
//    @GetAllStockParametersEndpoint
//    public ResponseEntity<List<StockParameterResponseDTO>> getAllStockParameters() {
//        var list = service.findAll();
//        return ResponseEntity.ok(list);
//    }
//
//    @GetStockParameterByIdEndpoint
//    public ResponseEntity<StockParameterResponseDTO> getById(@PathVariable Long id) {
//        var response = service.findById(id);
//        return ResponseEntity.ok(response);
//    }
//
//    @UpdateStockParameterEndpoint
//    public ResponseEntity<StockParameterResponseDTO> update(
//            @PathVariable Long id,
//            @Valid @RequestBody StockParameterRequestDTO request
//    ) {
//        var response = service.update(id, request);
//        return ResponseEntity.ok(response);
//    }
//
//    @DeleteStockParameterEndpoint
//    public ResponseEntity<Void> delete(@PathVariable Long id) {
//        service.delete(id);
//        return ResponseEntity.noContent().build();
//    }
}

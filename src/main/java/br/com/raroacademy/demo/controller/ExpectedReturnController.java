package br.com.raroacademy.demo.controller;

import br.com.raroacademy.demo.commons.annotations.ApiController;
import br.com.raroacademy.demo.commons.annotations.OpenApiController;
import br.com.raroacademy.demo.domain.DTO.expectedReturn.ExpectedReturnRequestDTO;
import br.com.raroacademy.demo.domain.DTO.expectedReturn.ExpectedReturnResponseDTO;
import br.com.raroacademy.demo.domain.annotations.expectedReturn.CreateExpectedReturnEndpoint;
import br.com.raroacademy.demo.domain.annotations.expectedReturn.DeleteExpectedReturnEndpoint;
import br.com.raroacademy.demo.domain.annotations.expectedReturn.GetAllExpectedReturnEndpoint;
import br.com.raroacademy.demo.domain.annotations.expectedReturn.GetExpectedReturnEndpoint;
import br.com.raroacademy.demo.domain.annotations.expectedReturn.UpdateExpectedReturnEndpoint;
import br.com.raroacademy.demo.service.ExpectedReturnService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@OpenApiController(name = "Expected Returns")
@ApiController(path = "/expected-returns")
@AllArgsConstructor
public class ExpectedReturnController {

    private final ExpectedReturnService expectedReturnService;

    @CreateExpectedReturnEndpoint
    public ResponseEntity<ExpectedReturnResponseDTO> create(@RequestBody @Valid ExpectedReturnRequestDTO dto) {
        var response = expectedReturnService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetExpectedReturnEndpoint
    public ResponseEntity<ExpectedReturnResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(expectedReturnService.getById(id));
    }

    @UpdateExpectedReturnEndpoint
    public ResponseEntity<ExpectedReturnResponseDTO> update(
            @PathVariable Long id,
            @RequestBody @Valid ExpectedReturnRequestDTO request) {
        return ResponseEntity.ok(expectedReturnService.update(id, request));
    }

    @DeleteExpectedReturnEndpoint
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        expectedReturnService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetAllExpectedReturnEndpoint
    public ResponseEntity<List<ExpectedReturnResponseDTO>> getAll() {
        return ResponseEntity.ok(expectedReturnService.getAll());
    }
}
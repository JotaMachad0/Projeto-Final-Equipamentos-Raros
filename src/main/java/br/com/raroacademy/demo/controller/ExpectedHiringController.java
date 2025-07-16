package br.com.raroacademy.demo.controller;

import br.com.raroacademy.demo.commons.i18n.I18nUtil;
import br.com.raroacademy.demo.commons.annotations.ApiController;
import br.com.raroacademy.demo.commons.annotations.OpenApiController;
import br.com.raroacademy.demo.domain.DTO.expected.hirings.ExpectedHiringRequestDTO;
import br.com.raroacademy.demo.domain.DTO.expected.hirings.ExpectedHiringResponseDTO;
import br.com.raroacademy.demo.domain.annotations.expected.hiring.*;
import br.com.raroacademy.demo.service.ExpectedHiringService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@OpenApiController(name = "Expected hirings")
@ApiController(path = "/expected-hirings")
@RequiredArgsConstructor
public class ExpectedHiringController {

    private final ExpectedHiringService expectedHiringService;
    private final I18nUtil i18nUtil;

    @CreateExpectedHiringEndpoint
    public ResponseEntity<ExpectedHiringResponseDTO> createExpectedHiring
            (@RequestBody @Valid ExpectedHiringRequestDTO request) {
        var response = expectedHiringService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetExpectedHiringEndpoint
    public ResponseEntity<ExpectedHiringResponseDTO> getExpectedHiring(@PathVariable Long id) {
        var expectedHiring = expectedHiringService.getExpectedHiringById(id);
        return ResponseEntity.ok(expectedHiring);
    }

    @UpdateExpectedHiringEndpoint
    public ResponseEntity<ExpectedHiringResponseDTO> updateExpectedHiring(
            @PathVariable Long id,
            @RequestBody @Valid ExpectedHiringRequestDTO request
    ) {
        var response = expectedHiringService.update(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteExpectedHiringEndpoint
    public ResponseEntity<Void> deleteExpectedHiring(@PathVariable Long id) {
        expectedHiringService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetAllExpectedHiringsEndpoint
    public ResponseEntity<List<ExpectedHiringResponseDTO>> getAllExpectedHirings() {
        var expectedHirings = expectedHiringService.getAllExpectedHirings();
        return ResponseEntity.ok(expectedHirings);
    }

    @StartExpectedHiringEndpoint
    public ResponseEntity<?> startHiring(@PathVariable Long id) {
        expectedHiringService.markAsProcessed(id);
        return ResponseEntity.ok(i18nUtil.getMessage("expected.hiring.status.update"));
    }
}

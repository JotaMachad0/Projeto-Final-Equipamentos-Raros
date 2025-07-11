package br.com.raroacademy.demo.controller;

import br.com.raroacademy.demo.domain.DTO.expected.hirings.ExpectedHiringRequestDTO;
import br.com.raroacademy.demo.domain.DTO.expected.hirings.ExpectedHiringResponseDTO;
import br.com.raroacademy.demo.service.ExpectedHiringService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/expected-hirings")
@RequiredArgsConstructor
public class ExpectedHiringController {

    private final ExpectedHiringService expectedHiringService;

    @PostMapping
    public ResponseEntity<ExpectedHiringResponseDTO> createExpectedHiring
            (@RequestBody @Valid ExpectedHiringRequestDTO request) {
        var response = expectedHiringService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExpectedHiringResponseDTO> getExpectedHiring(@PathVariable Long id) {
        var expectedHiring = expectedHiringService.getExpectedHiringById(id);
        return ResponseEntity.ok(expectedHiring);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExpectedHiringResponseDTO> updateExpectedHiring(
            @PathVariable Long id, @RequestBody @Valid ExpectedHiringRequestDTO request) {
        var response = expectedHiringService.update(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpectedHiring(@PathVariable Long id) {
        expectedHiringService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<ExpectedHiringResponseDTO>> getAllExpectedHirings() {
        var expectedHirings = expectedHiringService.getAllExpectedHirings();
        return ResponseEntity.ok(expectedHirings);
    }
}

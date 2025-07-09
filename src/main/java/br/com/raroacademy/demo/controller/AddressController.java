package br.com.raroacademy.demo.controller;

import br.com.raroacademy.demo.domain.DTO.address.AddressRequestDTO;
import br.com.raroacademy.demo.domain.DTO.address.AddressResponseDTO;
import br.com.raroacademy.demo.service.AddressService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/enderecos")
public class AddressController {

    @Autowired
    private AddressService service;

    @PostMapping
    public ResponseEntity<AddressResponseDTO> criar(@RequestBody @Valid AddressRequestDTO dto) {
        return ResponseEntity.ok(service.save(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AddressResponseDTO> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<AddressResponseDTO>> listarTodos() {
        return ResponseEntity.ok(service.findAll());
    }
}

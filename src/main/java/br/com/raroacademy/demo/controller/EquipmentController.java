package br.com.raroacademy.demo.controller;

import br.com.raroacademy.demo.domain.DTO.equipment.EquipmentRequestDTO;
import br.com.raroacademy.demo.domain.DTO.equipment.EquipmentResponseDTO;
import br.com.raroacademy.demo.domain.annotations.equipment.CreateEquipmentEndpoint;
import br.com.raroacademy.demo.domain.annotations.equipment.GetAllEquipmentsEndpoint;
import br.com.raroacademy.demo.service.EquipmentService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/equipments")
public class EquipmentController {

    private final EquipmentService service;

    public EquipmentController(EquipmentService service) {
        this.service = service;
    }

    @PostMapping
    @CreateEquipmentEndpoint
    public EquipmentResponseDTO create(@RequestBody @Valid EquipmentRequestDTO dto) {
        return service.create(dto);
    }

    @GetMapping
    @GetAllEquipmentsEndpoint
    public List<EquipmentResponseDTO> getAll() {
        return service.getAll();
    }
}

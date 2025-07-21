package br.com.raroacademy.demo.controller;

import br.com.raroacademy.demo.domain.DTO.equipment.returns.CreateReturnScheduleRequestDTO;
import br.com.raroacademy.demo.domain.DTO.equipment.returns.ProcessReturnRequestDTO;
import br.com.raroacademy.demo.domain.DTO.equipment.returns.EquipmentReturnResponseDTO;
import br.com.raroacademy.demo.service.EquipmentReturnService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/equipment-returns")
@AllArgsConstructor
public class EquipmentReturnController {

    private final EquipmentReturnService equipmentReturnService;

    @PostMapping("/schedule")
    public ResponseEntity<EquipmentReturnResponseDTO> createSchedule(@RequestBody @Valid CreateReturnScheduleRequestDTO request) {
        var response = equipmentReturnService.createSchedule(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/{id}/process")
    public ResponseEntity<EquipmentReturnResponseDTO> processReturn(
            @PathVariable("id") Long returnId,
            @RequestBody @Valid ProcessReturnRequestDTO request) {
        var response = equipmentReturnService.processReturn(returnId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<EquipmentReturnResponseDTO>> getAllReturns() {
        List<EquipmentReturnResponseDTO> allReturns = equipmentReturnService.getAll();
        return ResponseEntity.ok(allReturns);
    }

}
package br.com.raroacademy.demo.controller;

import br.com.raroacademy.demo.domain.DTO.collaborator.CollaboratorRequestDTO;
import br.com.raroacademy.demo.domain.DTO.collaborator.CollaboratorResponseDTO;
import br.com.raroacademy.demo.service.CollaboratorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/collaborators")
public class CollaboratorController {
    @Autowired
    private CollaboratorService  collaboratorService;

    @PostMapping
    public ResponseEntity<CollaboratorResponseDTO> createCollaborator(@RequestBody @Valid CollaboratorRequestDTO dto) {
        return ResponseEntity.ok(collaboratorService.save(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CollaboratorResponseDTO> getCollaborator(@PathVariable Long id) {
        return ResponseEntity.ok(collaboratorService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<CollaboratorResponseDTO>> getAllCollaborators() {
        return ResponseEntity.ok(collaboratorService.getAll());
    }
}

package br.com.raroacademy.demo.controller;

import br.com.raroacademy.demo.commons.annotations.ApiController;
import br.com.raroacademy.demo.commons.annotations.OpenApiController;
import br.com.raroacademy.demo.domain.DTO.collaborator.CollaboratorRequestDTO;
import br.com.raroacademy.demo.domain.DTO.collaborator.CollaboratorResponseDTO;
import br.com.raroacademy.demo.domain.DTO.collaborator.DismissalRequestDTO;
import br.com.raroacademy.demo.domain.DTO.collaborator.DismissalResponseDTO;
import br.com.raroacademy.demo.service.CollaboratorService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import br.com.raroacademy.demo.domain.annotations.collaborator.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@OpenApiController(name = "Collaborators")
@ApiController(path = "/collaborators")
@AllArgsConstructor
public class CollaboratorController {

    public final CollaboratorService collaboratorService;

    @CreateCollaboratorEndpoint
    public ResponseEntity<CollaboratorResponseDTO> createCollaborator(@RequestBody @Valid CollaboratorRequestDTO dto) {
        return ResponseEntity.ok(collaboratorService.save(dto));
    }

    @GetCollaboratorEndpoint
    public ResponseEntity<CollaboratorResponseDTO> getCollaborator(@PathVariable Long id) {
        return ResponseEntity.ok(collaboratorService.getById(id));
    }

    @GetAllCollaboratorsEndpoint
    public ResponseEntity<List<CollaboratorResponseDTO>> getAllCollaborators() {
        return ResponseEntity.ok(collaboratorService.getAll());
    }

    @UpdateCollaboratorEndpoint
    public ResponseEntity<CollaboratorResponseDTO> updateCollaborator(@PathVariable Long id, @RequestBody @Valid CollaboratorRequestDTO dto) {
        return ResponseEntity.ok(collaboratorService.update(id, dto));
    }

    @DeleteCollaboratorEndpoint
    public ResponseEntity<Void> deleteCollaborator(@PathVariable Long id) {
        collaboratorService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @TerminateCollaboratorEndpoint
    @PatchMapping("/{id}/dismiss")
    public ResponseEntity<DismissalResponseDTO> dismissCollaborator(
            @PathVariable Long id,
            @RequestBody @Valid DismissalRequestDTO dto) {
        return ResponseEntity.ok(collaboratorService.dismiss(id, dto));
    }
}

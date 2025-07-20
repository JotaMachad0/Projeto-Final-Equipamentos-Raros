package br.com.raroacademy.demo.domain.DTO.expected.returns;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ExpectedReturnRequestDTO (
    @NotNull
    @Future
    @Schema(description = "Future date expected for the equipment return", example = "2026-07-31")
    LocalDate expectedReturnDate,

    @NotNull
    @Schema(description = "ID of the equipment-collaborator link to which this return refers", example = "1")
    Long equipmentCollaboratorId) {
}
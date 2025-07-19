package br.com.raroacademy.demo.domain.DTO.expectedReturn;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ExpectedReturnRequestDTO {

    @NotNull
    @Future
    @Schema(description = "Future date expected for the equipment return", example = "2026-07-31")
    private LocalDate expectedReturnDate;

    @NotNull
    @Schema(description = "ID of the equipment-collaborator link to which this return refers", example = "1")
    private Long equipmentCollaboratorId;
}
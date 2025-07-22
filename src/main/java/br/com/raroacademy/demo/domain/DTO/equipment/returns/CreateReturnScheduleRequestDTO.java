package br.com.raroacademy.demo.domain.DTO.equipment.returns;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

public record CreateReturnScheduleRequestDTO(
        @NotNull
        @PastOrPresent
        LocalDate deliveryDate,

        @NotNull
        Long equipmentCollaboratorId
) {}
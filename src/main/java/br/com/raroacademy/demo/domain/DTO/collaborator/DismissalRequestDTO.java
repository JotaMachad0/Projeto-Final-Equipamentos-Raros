package br.com.raroacademy.demo.domain.DTO.collaborator;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record DismissalRequestDTO(
        @NotNull(message = "The dismissal date cannot be null.")
        LocalDate dismissalDate
) {}
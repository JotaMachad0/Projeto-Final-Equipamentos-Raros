package br.com.raroacademy.demo.domain.DTO.collaborator;


import java.time.LocalDate;
import java.util.List;

public record DismissalResponseDTO(
        Long id,
        String name,
        String email,
        LocalDate dismissalDate,
        List<ActiveLoanDTO> activeLoans
) {
    public record ActiveLoanDTO(
            Long loanId,
            Long equipmentId,
            String equipmentName,
            LocalDate loanDate
    ) {}
}
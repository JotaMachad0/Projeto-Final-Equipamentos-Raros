package br.com.raroacademy.demo.domain.DTO.collaborator;

import br.com.raroacademy.demo.domain.DTO.address.AddressResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDate;

@Getter
@Builder
public class CollaboratorResponseDTO {
    @Schema(example = "1")
    private Long id;

    @Schema(example = "Collaborator's name")
    private String name;

    @Schema(example = "123.456.789-00")
    private String cpf;

    @Schema(example = "collaborator@email.com")
    private String email;

    @Schema(example = "31999998888")
    private String phone;

    @Schema(example = "2024-01-15")
    private LocalDate contractStartDate;

    @Schema(example = "2025-01-14")
    private LocalDate contractEndDate;

    private AddressResponseDTO address;
}

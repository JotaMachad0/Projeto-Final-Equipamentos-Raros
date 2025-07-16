package br.com.raroacademy.demo.domain.DTO.equipment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Schema(name = "EquipmentRequestDTO", description = "DTO para criação de equipamento")
public class EquipmentRequestDTO {

    @NotNull(message = "ID é obrigatório")
    @Schema(description = "ID do equipamento", example = "1")
    private Long id;

    @NotBlank(message = "Tipo é obrigatório")
    @Schema(description = "Tipo do equipamento", example = "Notebook")
    private String type;

    @NotBlank(message = "Número de série é obrigatório")
    @Schema(description = "Número de série único", example = "SN-123456789")
    private String serialNumber;

    @NotBlank(message = "Marca é obrigatória")
    @Schema(description = "Marca do equipamento", example = "Dell")
    private String brand;

    @NotBlank(message = "Modelo é obrigatório")
    @Schema(description = "Modelo do equipamento", example = "Inspiron 15 3000")
    private String model;

    @Schema(description = "Especificações adicionais", example = "8GB RAM, SSD 256GB")
    private String specs;

    @NotNull(message = "Data de aquisição é obrigatória")
    @Schema(description = "Data de aquisição", example = "2024-01-15")
    private LocalDate acquisitionDate;

    @NotNull(message = "Tempo de uso (meses) é obrigatório")
    @Schema(description = "Tempo de uso em meses", example = "12")
    private Integer usageTimeMonths;

    @NotBlank(message = "Status é obrigatório")
    @Schema(description = "Status atual do equipamento", example = "EM_USO")
    private String status;
}

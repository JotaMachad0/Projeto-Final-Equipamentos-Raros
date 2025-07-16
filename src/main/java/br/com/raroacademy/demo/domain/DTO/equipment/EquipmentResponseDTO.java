package br.com.raroacademy.demo.domain.DTO.equipment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
@Schema(name = "EquipmentResponseDTO", description = "DTO de resposta com informações do equipamento")
public class EquipmentResponseDTO {

    @Schema(description = "ID do equipamento", example = "1")
    private Long id;

    @Schema(description = "Tipo do equipamento", example = "Notebook")
    private String type;

    @Schema(description = "Número de série único", example = "SN-123456789")
    private String serialNumber;

    @Schema(description = "Marca do equipamento", example = "Dell")
    private String brand;

    @Schema(description = "Modelo do equipamento", example = "Inspiron 15 3000")
    private String model;

    @Schema(description = "Especificações adicionais", example = "8GB RAM, SSD 256GB")
    private String specs;

    @Schema(description = "Data de aquisição", example = "2024-01-15")
    private LocalDate acquisitionDate;

    @Schema(description = "Tempo de uso em meses", example = "12")
    private Integer usageTimeMonths;

    @Schema(description = "Status atual do equipamento", example = "EM_USO")
    private String status;
}

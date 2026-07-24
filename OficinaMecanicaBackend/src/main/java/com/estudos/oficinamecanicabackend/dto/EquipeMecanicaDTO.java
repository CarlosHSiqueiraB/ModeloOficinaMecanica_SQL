package com.estudos.oficinamecanicabackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

// ===========================================
// DTO de entrada para EquipeMecanica (usado no POST e PUT)
// ===========================================
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EquipeMecanicaDTO {

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @NotNull(message = "ID do mecânico é obrigatório")
    private Long mecanicoId;
}

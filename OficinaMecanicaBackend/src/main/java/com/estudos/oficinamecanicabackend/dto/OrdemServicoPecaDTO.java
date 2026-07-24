package com.estudos.oficinamecanicabackend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

// ===========================================
// DTO para adicionar uma peça a uma Ordem de Serviço
// ===========================================
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrdemServicoPecaDTO {

    @NotNull(message = "ID da peça é obrigatório")
    private Long pecaId;

    @NotNull(message = "Quantidade é obrigatória")
    @Positive(message = "Quantidade deve ser maior que zero")
    private Integer quantidade;
}

package com.estudos.oficinamecanicabackend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

// ===========================================
// DTO para adicionar um serviço a uma Ordem de Serviço
// ===========================================
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrdemServicoServicoDTO {

    @NotNull(message = "ID do serviço é obrigatório")
    private Long servicoId;
}

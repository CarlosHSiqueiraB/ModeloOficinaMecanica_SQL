package com.estudos.oficinamecanicabackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

// ===========================================
// DTO de entrada para abrir uma Ordem de Serviço
// ===========================================
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrdemServicoDTO {

    @NotNull(message = "ID do veículo é obrigatório")
    private Long veiculoId;

    @NotNull(message = "ID da equipe é obrigatório")
    private Long equipeId;

    @NotBlank(message = "Problema relatado é obrigatório")
    private String problemaRelatado;

    private String observacoes;
}

package com.estudos.oficinamecanicabackend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

// ===========================================
// DTO de entrada para Servico (usado no POST e PUT)
// ===========================================
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServicoDTO {

    @NotNull(message = "Descrição é obrigatória")
    private String descricao;

    @NotNull(message = "Valor da mão de obra é obrigatório")
    private BigDecimal valorMaoDeObra;
}

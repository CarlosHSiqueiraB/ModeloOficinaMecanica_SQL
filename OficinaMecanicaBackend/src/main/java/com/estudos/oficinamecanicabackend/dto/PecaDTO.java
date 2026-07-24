package com.estudos.oficinamecanicabackend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

// ===========================================
// DTO de entrada para Peca (usado no POST e PUT)
// ===========================================
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PecaDTO {

    @NotNull(message = "Nome é obrigatório")
    private String nome;

    private String descricao;

    @NotNull(message = "Valor unitário é obrigatório")
    private BigDecimal valorUnitario;

    @NotNull(message = "Quantidade em estoque é obrigatória")
    private Integer quantidadeEstoque;
}

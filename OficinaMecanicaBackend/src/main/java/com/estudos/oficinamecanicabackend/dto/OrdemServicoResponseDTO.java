package com.estudos.oficinamecanicabackend.dto;

import com.estudos.oficinamecanicabackend.enums.StatusOS;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

// ===========================================
// DTO de saída para Ordem de Serviço (resposta da API)
// ===========================================
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrdemServicoResponseDTO {

    private Long id;
    private LocalDate dataAbertura;
    private LocalDate dataConclusao;
    private StatusOS status;
    private String observacoes;
    private BigDecimal valorTotal;
    private String problemaRelatado;
    private Long veiculoId;
    private String veiculoPlaca;
    private Long equipeId;
    private String equipeNome;
    private List<ItemServicoResponseDTO> servicos;
    private List<ItemPecaResponseDTO> pecas;

    // ===========================================
    // DTO interno para representar um serviço usado na resposta
    // ===========================================
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ItemServicoResponseDTO {
        private Long servicoId;
        private String descricao;
        private BigDecimal valorMaoDeObra;
    }

    // ===========================================
    // DTO interno para representar uma peça usada na resposta
    // ===========================================
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ItemPecaResponseDTO {
        private Long pecaId;
        private String pecaNome;
        private Integer quantidade;
        private BigDecimal valorUnitario;
        private BigDecimal subtotal;
    }
}

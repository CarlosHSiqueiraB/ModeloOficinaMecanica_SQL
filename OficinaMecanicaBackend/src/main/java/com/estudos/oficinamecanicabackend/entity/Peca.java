package com.estudos.oficinamecanicabackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

// ===========================================
// Entidade Peca - representa uma peça disponível na oficina
// Uma peça pode estar em várias ordens de serviço (N:N via OrdemServicoPeca)
// ===========================================
@Entity
@Table(name = "pecas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Peca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    private String descricao;

    // Preço unitário da peça
    @Column(nullable = false)
    private BigDecimal valorUnitario;

    // Estoque disponível
    @Column(nullable = false)
    private Integer quantidadeEstoque;

    // Relacionamento N:N com OrdemServico via tabela intermediária
    @JsonIgnore
    @OneToMany(mappedBy = "peca", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrdemServicoPeca> ordensServico = new ArrayList<>();
}

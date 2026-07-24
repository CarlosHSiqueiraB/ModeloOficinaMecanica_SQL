package com.estudos.oficinamecanicabackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

// ===========================================
// Entidade intermediária OrdemServicoPeca
// Resolve o relacionamento N:N entre OrdemServico e Peca
// Cada registro indica que uma determinada peça foi utilizada em uma OS
// Contém a quantidade utilizada da peça
// ===========================================
@Entity
@Table(name = "ordens_servico_pecas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrdemServicoPeca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Referência à ordem de serviço
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ordem_servico_id", nullable = false)
    private OrdemServico ordemServico;

    // Referência à peça utilizada
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "peca_id", nullable = false)
    private Peca peca;

    // Quantidade da peça utilizada na OS
    @Column(nullable = false)
    private Integer quantidade;
}

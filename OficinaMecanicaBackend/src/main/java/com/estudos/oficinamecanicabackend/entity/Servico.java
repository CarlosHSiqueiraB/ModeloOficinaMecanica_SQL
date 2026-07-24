package com.estudos.oficinamecanicabackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

// ===========================================
// Entidade Servico - representa um serviço oferecido pela oficina
// Ex: troca de óleo, alinhamento,平衡amento
// Um serviço pode estar em várias ordens de serviço (N:N via OrdemServicoServico)
// ===========================================
@Entity
@Table(name = "servicos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Servico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String descricao;

    // Valor da mão de obra do serviço
    @Column(nullable = false)
    private BigDecimal valorMaoDeObra;

    // Relacionamento N:N com OrdemServico via tabela intermediária
    @JsonIgnore
    @OneToMany(mappedBy = "servico", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrdemServicoServico> ordensServico = new ArrayList<>();
}

package com.estudos.oficinamecanicabackend.entity;

import com.estudos.oficinamecanicabackend.enums.StatusOS;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// ===========================================
// Entidade OrdemServico - representa uma ordem de serviço
// Uma OS pertence a um veículo (N:1)
// Uma OS é atendida por uma equipe (N:1)
// Uma OS pode ter vários serviços (N:N via OrdemServicoServico)
// Uma OS pode usar várias peças (N:N via OrdemServicoPeca)
// ===========================================
@Entity
@Table(name = "ordens_servico")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrdemServico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Data de abertura da OS
    @Column(nullable = false)
    private LocalDate dataAbertura;

    // Data de conclusão da OS (pode ser nula se ainda estiver em andamento)
    private LocalDate dataConclusao;

    // Status atual da ordem de serviço
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusOS status;

    // Observações gerais sobre o serviço
    @Column(columnDefinition = "TEXT")
    private String observacoes;

    // Valor total da OS (calculado automaticamente)
    @Column(nullable = false)
    private BigDecimal valorTotal;

    // Descrição do problema relatado pelo cliente
    @Column(nullable = false)
    private String problemaRelatado;

    // Cada OS pertence a um veículo
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "veiculo_id", nullable = false)
    private Veiculo veiculo;

    // Cada OS é atendida por uma equipe
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipe_id", nullable = false)
    private EquipeMecanica equipe;

    // Serviços associados a esta OS (N:N)
    @OneToMany(mappedBy = "ordemServico", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrdemServicoServico> servicos = new ArrayList<>();

    // Peças utilizadas nesta OS (N:N)
    @OneToMany(mappedBy = "ordemServico", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrdemServicoPeca> pecas = new ArrayList<>();

    // ===========================================
    // Método que calcula o valor total da OS
    // Soma: (valor mão de obra dos serviços) + (valor unitário × quantidade das peças)
    // ===========================================
    public void calcularValorTotal() {
        BigDecimal totalServicos = servicos.stream()
                .map(os -> os.getServico().getValorMaoDeObra())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalPecas = pecas.stream()
                .map(os -> os.getPeca().getValorUnitario()
                        .multiply(BigDecimal.valueOf(os.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.valorTotal = totalServicos.add(totalPecas);
    }
}

package com.estudos.oficinamecanicabackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

// ===========================================
// Entidade EquipeMecanica - representa a equipe de mecânicos
// Uma equipe tem um mecânico responsável (N:1)
// Uma equipe pode ter várias ordens de serviço (1:N)
// ===========================================
@Entity
@Table(name = "equipes_mecanicas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EquipeMecanica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    // Cada equipe tem um mecânico responsável
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mecanico_id", nullable = false)
    private Mecanico mecanico;

    // Uma equipe pode atender várias ordens de serviço
    @JsonIgnore
    @OneToMany(mappedBy = "equipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrdemServico> ordensServico = new ArrayList<>();
}

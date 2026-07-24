package com.estudos.oficinamecanicabackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

// ===========================================
// Entidade Mecanico - representa o mecânico da oficina
// Um mecânico pode pertencer a várias equipes (1:N)
// ===========================================
@Entity
@Table(name = "mecanicos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Mecanico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String cpf;

    private String especialidade;

    private String telefone;

    // Um mecânico pode estar em várias equipes
    @JsonIgnore
    @OneToMany(mappedBy = "mecanico", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EquipeMecanica> equipes = new ArrayList<>();
}

package com.estudos.oficinamecanicabackend.repository;

import com.estudos.oficinamecanicabackend.entity.Servico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// ===========================================
// Repository da entidade Servico
// ===========================================
@Repository
public interface ServicoRepository extends JpaRepository<Servico, Long> {
}

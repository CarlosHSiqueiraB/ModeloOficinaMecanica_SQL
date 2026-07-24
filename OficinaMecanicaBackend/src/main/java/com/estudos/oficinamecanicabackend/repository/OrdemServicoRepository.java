package com.estudos.oficinamecanicabackend.repository;

import com.estudos.oficinamecanicabackend.entity.OrdemServico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// ===========================================
// Repository da entidade OrdemServico
// ===========================================
@Repository
public interface OrdemServicoRepository extends JpaRepository<OrdemServico, Long> {
}

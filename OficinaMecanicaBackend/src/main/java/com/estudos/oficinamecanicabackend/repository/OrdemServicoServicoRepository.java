package com.estudos.oficinamecanicabackend.repository;

import com.estudos.oficinamecanicabackend.entity.OrdemServicoServico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// ===========================================
// Repository da entidade intermediária OrdemServicoServico
// ===========================================
@Repository
public interface OrdemServicoServicoRepository extends JpaRepository<OrdemServicoServico, Long> {
}

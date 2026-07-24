package com.estudos.oficinamecanicabackend.repository;

import com.estudos.oficinamecanicabackend.entity.OrdemServicoPeca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// ===========================================
// Repository da entidade intermediária OrdemServicoPeca
// ===========================================
@Repository
public interface OrdemServicoPecaRepository extends JpaRepository<OrdemServicoPeca, Long> {
}

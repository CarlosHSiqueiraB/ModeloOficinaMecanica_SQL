package com.estudos.oficinamecanicabackend.repository;

import com.estudos.oficinamecanicabackend.entity.Peca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// ===========================================
// Repository da entidade Peca
// ===========================================
@Repository
public interface PecaRepository extends JpaRepository<Peca, Long> {
}

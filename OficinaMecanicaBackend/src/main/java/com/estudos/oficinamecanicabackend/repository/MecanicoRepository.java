package com.estudos.oficinamecanicabackend.repository;

import com.estudos.oficinamecanicabackend.entity.Mecanico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// ===========================================
// Repository da entidade Mecanico
// ===========================================
@Repository
public interface MecanicoRepository extends JpaRepository<Mecanico, Long> {
}

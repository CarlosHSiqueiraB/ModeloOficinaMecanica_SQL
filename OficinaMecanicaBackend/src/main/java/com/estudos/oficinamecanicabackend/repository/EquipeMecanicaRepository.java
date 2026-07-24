package com.estudos.oficinamecanicabackend.repository;

import com.estudos.oficinamecanicabackend.entity.EquipeMecanica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// ===========================================
// Repository da entidade EquipeMecanica
// ===========================================
@Repository
public interface EquipeMecanicaRepository extends JpaRepository<EquipeMecanica, Long> {
}

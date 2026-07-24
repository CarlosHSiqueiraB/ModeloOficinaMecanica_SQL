package com.estudos.oficinamecanicabackend.repository;

import com.estudos.oficinamecanicabackend.entity.Veiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// ===========================================
// Repository da entidade Veiculo
// ===========================================
@Repository
public interface VeiculoRepository extends JpaRepository<Veiculo, Long> {
}

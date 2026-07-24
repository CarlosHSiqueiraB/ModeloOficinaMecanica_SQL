package com.estudos.oficinamecanicabackend.repository;

import com.estudos.oficinamecanicabackend.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// ===========================================
// Repository da entidade Cliente
// Fornece operações CRUD básicas via JpaRepository
// ===========================================
@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}

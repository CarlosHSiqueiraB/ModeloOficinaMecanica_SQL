package com.estudos.oficinamecanicabackend.controller;

import com.estudos.oficinamecanicabackend.dto.MecanicoDTO;
import com.estudos.oficinamecanicabackend.entity.Mecanico;
import com.estudos.oficinamecanicabackend.service.MecanicoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// ===========================================
// Controller do Mecanico
// Endpoints REST para CRUD de mecânicos
// Base: /api/mecanicos
// ===========================================
@RestController
@RequestMapping("/api/mecanicos")
@RequiredArgsConstructor
public class MecanicoController {

    private final MecanicoService mecanicoService;

    // GET /api/mecanicos - Lista todos os mecânicos
    @GetMapping
    public ResponseEntity<List<Mecanico>> listarTodos() {
        return ResponseEntity.ok(mecanicoService.listarTodos());
    }

    // GET /api/mecanicos/{id} - Busca um mecânico por ID
    @GetMapping("/{id}")
    public ResponseEntity<Mecanico> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(mecanicoService.buscarPorId(id));
    }

    // POST /api/mecanicos - Cadastra um novo mecânico
    @PostMapping
    public ResponseEntity<Mecanico> cadastrar(@Valid @RequestBody MecanicoDTO dto) {
        Mecanico mecanico = mecanicoService.cadastrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(mecanico);
    }

    // PUT /api/mecanicos/{id} - Atualiza um mecânico existente
    @PutMapping("/{id}")
    public ResponseEntity<Mecanico> atualizar(@PathVariable Long id, @Valid @RequestBody MecanicoDTO dto) {
        return ResponseEntity.ok(mecanicoService.atualizar(id, dto));
    }

    // DELETE /api/mecanicos/{id} - Exclui um mecânico
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        mecanicoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}

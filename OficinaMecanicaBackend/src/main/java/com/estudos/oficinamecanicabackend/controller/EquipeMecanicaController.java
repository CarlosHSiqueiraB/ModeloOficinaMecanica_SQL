package com.estudos.oficinamecanicabackend.controller;

import com.estudos.oficinamecanicabackend.dto.EquipeMecanicaDTO;
import com.estudos.oficinamecanicabackend.entity.EquipeMecanica;
import com.estudos.oficinamecanicabackend.service.EquipeMecanicaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// ===========================================
// Controller da EquipeMecanica
// Endpoints REST para CRUD de equipes mecânicas
// Base: /api/equipes
// ===========================================
@RestController
@RequestMapping("/api/equipes")
@RequiredArgsConstructor
public class EquipeMecanicaController {

    private final EquipeMecanicaService equipeMecanicaService;

    // GET /api/equipes - Lista todas as equipes
    @GetMapping
    public ResponseEntity<List<EquipeMecanica>> listarTodos() {
        return ResponseEntity.ok(equipeMecanicaService.listarTodos());
    }

    // GET /api/equipes/{id} - Busca uma equipe por ID
    @GetMapping("/{id}")
    public ResponseEntity<EquipeMecanica> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(equipeMecanicaService.buscarPorId(id));
    }

    // POST /api/equipes - Cadastra uma nova equipe
    @PostMapping
    public ResponseEntity<EquipeMecanica> cadastrar(@Valid @RequestBody EquipeMecanicaDTO dto) {
        EquipeMecanica equipe = equipeMecanicaService.cadastrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(equipe);
    }

    // PUT /api/equipes/{id} - Atualiza uma equipe existente
    @PutMapping("/{id}")
    public ResponseEntity<EquipeMecanica> atualizar(@PathVariable Long id, @Valid @RequestBody EquipeMecanicaDTO dto) {
        return ResponseEntity.ok(equipeMecanicaService.atualizar(id, dto));
    }

    // DELETE /api/equipes/{id} - Exclui uma equipe
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        equipeMecanicaService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}

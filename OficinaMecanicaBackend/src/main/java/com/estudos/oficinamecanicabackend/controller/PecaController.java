package com.estudos.oficinamecanicabackend.controller;

import com.estudos.oficinamecanicabackend.dto.PecaDTO;
import com.estudos.oficinamecanicabackend.entity.Peca;
import com.estudos.oficinamecanicabackend.service.PecaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// ===========================================
// Controller da Peca
// Endpoints REST para CRUD de peças
// Base: /api/pecas
// ===========================================
@RestController
@RequestMapping("/api/pecas")
@RequiredArgsConstructor
public class PecaController {

    private final PecaService pecaService;

    // GET /api/pecas - Lista todas as peças
    @GetMapping
    public ResponseEntity<List<Peca>> listarTodos() {
        return ResponseEntity.ok(pecaService.listarTodos());
    }

    // GET /api/pecas/{id} - Busca uma peça por ID
    @GetMapping("/{id}")
    public ResponseEntity<Peca> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pecaService.buscarPorId(id));
    }

    // POST /api/pecas - Cadastra uma nova peça
    @PostMapping
    public ResponseEntity<Peca> cadastrar(@Valid @RequestBody PecaDTO dto) {
        Peca peca = pecaService.cadastrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(peca);
    }

    // PUT /api/pecas/{id} - Atualiza uma peça existente
    @PutMapping("/{id}")
    public ResponseEntity<Peca> atualizar(@PathVariable Long id, @Valid @RequestBody PecaDTO dto) {
        return ResponseEntity.ok(pecaService.atualizar(id, dto));
    }

    // DELETE /api/pecas/{id} - Exclui uma peça
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        pecaService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}

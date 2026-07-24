package com.estudos.oficinamecanicabackend.controller;

import com.estudos.oficinamecanicabackend.dto.ServicoDTO;
import com.estudos.oficinamecanicabackend.entity.Servico;
import com.estudos.oficinamecanicabackend.service.ServicoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// ===========================================
// Controller do Servico
// Endpoints REST para CRUD de serviços
// Base: /api/servicos
// ===========================================
@RestController
@RequestMapping("/api/servicos")
@RequiredArgsConstructor
public class ServicoController {

    private final ServicoService servicoService;

    // GET /api/servicos - Lista todos os serviços
    @GetMapping
    public ResponseEntity<List<Servico>> listarTodos() {
        return ResponseEntity.ok(servicoService.listarTodos());
    }

    // GET /api/servicos/{id} - Busca um serviço por ID
    @GetMapping("/{id}")
    public ResponseEntity<Servico> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(servicoService.buscarPorId(id));
    }

    // POST /api/servicos - Cadastra um novo serviço
    @PostMapping
    public ResponseEntity<Servico> cadastrar(@Valid @RequestBody ServicoDTO dto) {
        Servico servico = servicoService.cadastrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(servico);
    }

    // PUT /api/servicos/{id} - Atualiza um serviço existente
    @PutMapping("/{id}")
    public ResponseEntity<Servico> atualizar(@PathVariable Long id, @Valid @RequestBody ServicoDTO dto) {
        return ResponseEntity.ok(servicoService.atualizar(id, dto));
    }

    // DELETE /api/servicos/{id} - Exclui um serviço
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        servicoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}

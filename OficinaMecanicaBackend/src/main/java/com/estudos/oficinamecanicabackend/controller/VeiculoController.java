package com.estudos.oficinamecanicabackend.controller;

import com.estudos.oficinamecanicabackend.dto.VeiculoDTO;
import com.estudos.oficinamecanicabackend.entity.Veiculo;
import com.estudos.oficinamecanicabackend.service.VeiculoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// ===========================================
// Controller do Veiculo
// Endpoints REST para CRUD de veículos
// Base: /api/veiculos
// ===========================================
@RestController
@RequestMapping("/api/veiculos")
@RequiredArgsConstructor
public class VeiculoController {

    private final VeiculoService veiculoService;

    // GET /api/veiculos - Lista todos os veículos
    @GetMapping
    public ResponseEntity<List<Veiculo>> listarTodos() {
        return ResponseEntity.ok(veiculoService.listarTodos());
    }

    // GET /api/veiculos/{id} - Busca um veículo por ID
    @GetMapping("/{id}")
    public ResponseEntity<Veiculo> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(veiculoService.buscarPorId(id));
    }

    // POST /api/veiculos - Cadastra um novo veículo
    @PostMapping
    public ResponseEntity<Veiculo> cadastrar(@Valid @RequestBody VeiculoDTO dto) {
        Veiculo veiculo = veiculoService.cadastrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(veiculo);
    }

    // PUT /api/veiculos/{id} - Atualiza um veículo existente
    @PutMapping("/{id}")
    public ResponseEntity<Veiculo> atualizar(@PathVariable Long id, @Valid @RequestBody VeiculoDTO dto) {
        return ResponseEntity.ok(veiculoService.atualizar(id, dto));
    }

    // DELETE /api/veiculos/{id} - Exclui um veículo
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        veiculoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}

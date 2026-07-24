package com.estudos.oficinamecanicabackend.controller;

import com.estudos.oficinamecanicabackend.dto.ClienteDTO;
import com.estudos.oficinamecanicabackend.entity.Cliente;
import com.estudos.oficinamecanicabackend.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// ===========================================
// Controller do Cliente
// Endpoints REST para CRUD de clientes
// Base: /api/clientes
// ===========================================
@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    // GET /api/clientes - Lista todos os clientes
    @GetMapping
    public ResponseEntity<List<Cliente>> listarTodos() {
        return ResponseEntity.ok(clienteService.listarTodos());
    }

    // GET /api/clientes/{id} - Busca um cliente por ID
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(clienteService.buscarPorId(id));
    }

    // POST /api/clientes - Cadastra um novo cliente
    @PostMapping
    public ResponseEntity<Cliente> cadastrar(@Valid @RequestBody ClienteDTO dto) {
        Cliente cliente = clienteService.cadastrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(cliente);
    }

    // PUT /api/clientes/{id} - Atualiza um cliente existente
    @PutMapping("/{id}")
    public ResponseEntity<Cliente> atualizar(@PathVariable Long id, @Valid @RequestBody ClienteDTO dto) {
        return ResponseEntity.ok(clienteService.atualizar(id, dto));
    }

    // DELETE /api/clientes/{id} - Exclui um cliente
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        clienteService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}

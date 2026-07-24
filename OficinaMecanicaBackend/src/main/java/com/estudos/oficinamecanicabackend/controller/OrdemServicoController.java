package com.estudos.oficinamecanicabackend.controller;

import com.estudos.oficinamecanicabackend.dto.*;
import com.estudos.oficinamecanicabackend.entity.OrdemServico;
import com.estudos.oficinamecanicabackend.service.OrdemServicoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

// ===========================================
// Controller da OrdemServico
// Endpoints REST para gerenciamento de Ordens de Serviço
// Base: /api/ordens-servico
// ===========================================
@RestController
@RequestMapping("/api/ordens-servico")
@RequiredArgsConstructor
public class OrdemServicoController {

    private final OrdemServicoService ordemServicoService;

    // GET /api/ordens-servico - Lista todas as OS (retorna DTO de resposta)
    @GetMapping
    public ResponseEntity<List<OrdemServicoResponseDTO>> listarTodos() {
        List<OrdemServicoResponseDTO> lista = ordemServicoService.listarTodos().stream()
                .map(ordemServicoService::paraResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    // GET /api/ordens-servico/{id} - Busca uma OS por ID
    @GetMapping("/{id}")
    public ResponseEntity<OrdemServicoResponseDTO> buscarPorId(@PathVariable Long id) {
        OrdemServico os = ordemServicoService.buscarPorId(id);
        return ResponseEntity.ok(ordemServicoService.paraResponseDTO(os));
    }

    // POST /api/ordens-servico - Abre uma nova Ordem de Serviço
    @PostMapping
    public ResponseEntity<OrdemServicoResponseDTO> abrirOrdemServico(@Valid @RequestBody OrdemServicoDTO dto) {
        OrdemServico os = ordemServicoService.abrirOrdemServico(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ordemServicoService.paraResponseDTO(os));
    }

    // PATCH /api/ordens-servico/{id}/status - Altera o status de uma OS
    @PatchMapping("/{id}/status")
    public ResponseEntity<OrdemServicoResponseDTO> alterarStatus(
            @PathVariable Long id,
            @Valid @RequestBody StatusOSDTO dto) {
        OrdemServico os = ordemServicoService.alterarStatus(id, dto);
        return ResponseEntity.ok(ordemServicoService.paraResponseDTO(os));
    }

    // POST /api/ordens-servico/{id}/servicos - Adiciona um serviço à OS
    @PostMapping("/{id}/servicos")
    public ResponseEntity<OrdemServicoResponseDTO> adicionarServico(
            @PathVariable Long id,
            @Valid @RequestBody OrdemServicoServicoDTO dto) {
        OrdemServico os = ordemServicoService.adicionarServico(id, dto);
        return ResponseEntity.ok(ordemServicoService.paraResponseDTO(os));
    }

    // DELETE /api/ordens-servico/{id}/servicos/{servicoId} - Remove um serviço da OS
    @DeleteMapping("/{id}/servicos/{servicoId}")
    public ResponseEntity<OrdemServicoResponseDTO> removerServico(
            @PathVariable Long id,
            @PathVariable Long servicoId) {
        OrdemServico os = ordemServicoService.removerServico(id, servicoId);
        return ResponseEntity.ok(ordemServicoService.paraResponseDTO(os));
    }

    // POST /api/ordens-servico/{id}/pecas - Adiciona uma peça à OS
    @PostMapping("/{id}/pecas")
    public ResponseEntity<OrdemServicoResponseDTO> adicionarPeca(
            @PathVariable Long id,
            @Valid @RequestBody OrdemServicoPecaDTO dto) {
        OrdemServico os = ordemServicoService.adicionarPeca(id, dto);
        return ResponseEntity.ok(ordemServicoService.paraResponseDTO(os));
    }

    // DELETE /api/ordens-servico/{id}/pecas/{pecaId} - Remove uma peça da OS
    @DeleteMapping("/{id}/pecas/{pecaId}")
    public ResponseEntity<OrdemServicoResponseDTO> removerPeca(
            @PathVariable Long id,
            @PathVariable Long pecaId) {
        OrdemServico os = ordemServicoService.removerPeca(id, pecaId);
        return ResponseEntity.ok(ordemServicoService.paraResponseDTO(os));
    }

    // DELETE /api/ordens-servico/{id} - Exclui uma OS
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        ordemServicoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}

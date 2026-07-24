package com.estudos.oficinamecanicabackend.service;

import com.estudos.oficinamecanicabackend.dto.PecaDTO;
import com.estudos.oficinamecanicabackend.entity.Peca;
import com.estudos.oficinamecanicabackend.exception.ResourceNotFoundException;
import com.estudos.oficinamecanicabackend.repository.PecaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// ===========================================
// Service da entidade Peca
// Responsável pela lógica de negócio do CRUD de peças
// ===========================================
@Service
@RequiredArgsConstructor
public class PecaService {

    private final PecaRepository pecaRepository;

    // Lista todas as peças
    @Transactional(readOnly = true)
    public List<Peca> listarTodos() {
        return pecaRepository.findAll();
    }

    // Busca uma peça por ID
    @Transactional(readOnly = true)
    public Peca buscarPorId(Long id) {
        return pecaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Peça não encontrada com id: " + id));
    }

    // Cadastra uma nova peça
    @Transactional
    public Peca cadastrar(PecaDTO dto) {
        Peca peca = Peca.builder()
                .nome(dto.getNome())
                .descricao(dto.getDescricao())
                .valorUnitario(dto.getValorUnitario())
                .quantidadeEstoque(dto.getQuantidadeEstoque())
                .build();
        return pecaRepository.save(peca);
    }

    // Atualiza uma peça existente
    @Transactional
    public Peca atualizar(Long id, PecaDTO dto) {
        Peca peca = buscarPorId(id);
        peca.setNome(dto.getNome());
        peca.setDescricao(dto.getDescricao());
        peca.setValorUnitario(dto.getValorUnitario());
        peca.setQuantidadeEstoque(dto.getQuantidadeEstoque());
        return pecaRepository.save(peca);
    }

    // Exclui uma peça por ID
    @Transactional
    public void excluir(Long id) {
        Peca peca = buscarPorId(id);
        pecaRepository.delete(peca);
    }
}

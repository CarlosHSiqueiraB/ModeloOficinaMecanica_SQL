package com.estudos.oficinamecanicabackend.service;

import com.estudos.oficinamecanicabackend.dto.ServicoDTO;
import com.estudos.oficinamecanicabackend.entity.Servico;
import com.estudos.oficinamecanicabackend.exception.ResourceNotFoundException;
import com.estudos.oficinamecanicabackend.repository.ServicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// ===========================================
// Service da entidade Servico
// Responsável pela lógica de negócio do CRUD de serviços
// ===========================================
@Service
@RequiredArgsConstructor
public class ServicoService {

    private final ServicoRepository servicoRepository;

    // Lista todos os serviços
    @Transactional(readOnly = true)
    public List<Servico> listarTodos() {
        return servicoRepository.findAll();
    }

    // Busca um serviço por ID
    @Transactional(readOnly = true)
    public Servico buscarPorId(Long id) {
        return servicoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Serviço não encontrado com id: " + id));
    }

    // Cadastra um novo serviço
    @Transactional
    public Servico cadastrar(ServicoDTO dto) {
        Servico servico = Servico.builder()
                .descricao(dto.getDescricao())
                .valorMaoDeObra(dto.getValorMaoDeObra())
                .build();
        return servicoRepository.save(servico);
    }

    // Atualiza um serviço existente
    @Transactional
    public Servico atualizar(Long id, ServicoDTO dto) {
        Servico servico = buscarPorId(id);
        servico.setDescricao(dto.getDescricao());
        servico.setValorMaoDeObra(dto.getValorMaoDeObra());
        return servicoRepository.save(servico);
    }

    // Exclui um serviço por ID
    @Transactional
    public void excluir(Long id) {
        Servico servico = buscarPorId(id);
        servicoRepository.delete(servico);
    }
}

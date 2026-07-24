package com.estudos.oficinamecanicabackend.service;

import com.estudos.oficinamecanicabackend.dto.MecanicoDTO;
import com.estudos.oficinamecanicabackend.entity.Mecanico;
import com.estudos.oficinamecanicabackend.exception.ResourceNotFoundException;
import com.estudos.oficinamecanicabackend.repository.MecanicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// ===========================================
// Service da entidade Mecanico
// Responsável pela lógica de negócio do CRUD de mecânicos
// ===========================================
@Service
@RequiredArgsConstructor
public class MecanicoService {

    private final MecanicoRepository mecanicoRepository;

    // Lista todos os mecânicos
    @Transactional(readOnly = true)
    public List<Mecanico> listarTodos() {
        return mecanicoRepository.findAll();
    }

    // Busca um mecânico por ID
    @Transactional(readOnly = true)
    public Mecanico buscarPorId(Long id) {
        return mecanicoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mecânico não encontrado com id: " + id));
    }

    // Cadastra um novo mecânico
    @Transactional
    public Mecanico cadastrar(MecanicoDTO dto) {
        Mecanico mecanico = Mecanico.builder()
                .nome(dto.getNome())
                .cpf(dto.getCpf())
                .especialidade(dto.getEspecialidade())
                .telefone(dto.getTelefone())
                .build();
        return mecanicoRepository.save(mecanico);
    }

    // Atualiza um mecânico existente
    @Transactional
    public Mecanico atualizar(Long id, MecanicoDTO dto) {
        Mecanico mecanico = buscarPorId(id);
        mecanico.setNome(dto.getNome());
        mecanico.setCpf(dto.getCpf());
        mecanico.setEspecialidade(dto.getEspecialidade());
        mecanico.setTelefone(dto.getTelefone());
        return mecanicoRepository.save(mecanico);
    }

    // Exclui um mecânico por ID
    @Transactional
    public void excluir(Long id) {
        Mecanico mecanico = buscarPorId(id);
        mecanicoRepository.delete(mecanico);
    }
}

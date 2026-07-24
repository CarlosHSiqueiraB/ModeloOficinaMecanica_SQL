package com.estudos.oficinamecanicabackend.service;

import com.estudos.oficinamecanicabackend.dto.EquipeMecanicaDTO;
import com.estudos.oficinamecanicabackend.entity.EquipeMecanica;
import com.estudos.oficinamecanicabackend.entity.Mecanico;
import com.estudos.oficinamecanicabackend.exception.ResourceNotFoundException;
import com.estudos.oficinamecanicabackend.repository.EquipeMecanicaRepository;
import com.estudos.oficinamecanicabackend.repository.MecanicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// ===========================================
// Service da entidade EquipeMecanica
// Responsável pela lógica de negócio do CRUD de equipes
// ===========================================
@Service
@RequiredArgsConstructor
public class EquipeMecanicaService {

    private final EquipeMecanicaRepository equipeMecanicaRepository;
    private final MecanicoRepository mecanicoRepository;

    // Lista todas as equipes
    @Transactional(readOnly = true)
    public List<EquipeMecanica> listarTodos() {
        return equipeMecanicaRepository.findAll();
    }

    // Busca uma equipe por ID
    @Transactional(readOnly = true)
    public EquipeMecanica buscarPorId(Long id) {
        return equipeMecanicaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipe não encontrada com id: " + id));
    }

    // Cadastra uma nova equipe vinculada a um mecânico
    @Transactional
    public EquipeMecanica cadastrar(EquipeMecanicaDTO dto) {
        Mecanico mecanico = mecanicoRepository.findById(dto.getMecanicoId())
                .orElseThrow(() -> new ResourceNotFoundException("Mecânico não encontrado com id: " + dto.getMecanicoId()));

        EquipeMecanica equipe = EquipeMecanica.builder()
                .nome(dto.getNome())
                .mecanico(mecanico)
                .build();
        return equipeMecanicaRepository.save(equipe);
    }

    // Atualiza uma equipe existente
    @Transactional
    public EquipeMecanica atualizar(Long id, EquipeMecanicaDTO dto) {
        EquipeMecanica equipe = buscarPorId(id);
        Mecanico mecanico = mecanicoRepository.findById(dto.getMecanicoId())
                .orElseThrow(() -> new ResourceNotFoundException("Mecânico não encontrado com id: " + dto.getMecanicoId()));

        equipe.setNome(dto.getNome());
        equipe.setMecanico(mecanico);
        return equipeMecanicaRepository.save(equipe);
    }

    // Exclui uma equipe por ID
    @Transactional
    public void excluir(Long id) {
        EquipeMecanica equipe = buscarPorId(id);
        equipeMecanicaRepository.delete(equipe);
    }
}

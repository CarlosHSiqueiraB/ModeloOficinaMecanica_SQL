package com.estudos.oficinamecanicabackend.service;

import com.estudos.oficinamecanicabackend.dto.VeiculoDTO;
import com.estudos.oficinamecanicabackend.entity.Cliente;
import com.estudos.oficinamecanicabackend.entity.Veiculo;
import com.estudos.oficinamecanicabackend.exception.ResourceNotFoundException;
import com.estudos.oficinamecanicabackend.repository.ClienteRepository;
import com.estudos.oficinamecanicabackend.repository.VeiculoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// ===========================================
// Service da entidade Veiculo
// Responsável pela lógica de negócio do CRUD de veículos
// ===========================================
@Service
@RequiredArgsConstructor
public class VeiculoService {

    private final VeiculoRepository veiculoRepository;
    private final ClienteRepository clienteRepository;

    // Lista todos os veículos
    @Transactional(readOnly = true)
    public List<Veiculo> listarTodos() {
        return veiculoRepository.findAll();
    }

    // Busca um veículo por ID
    @Transactional(readOnly = true)
    public Veiculo buscarPorId(Long id) {
        return veiculoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Veículo não encontrado com id: " + id));
    }

    // Cadastra um novo veículo vinculado a um cliente
    @Transactional
    public Veiculo cadastrar(VeiculoDTO dto) {
        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com id: " + dto.getClienteId()));

        Veiculo veiculo = Veiculo.builder()
                .marca(dto.getMarca())
                .modelo(dto.getModelo())
                .ano(dto.getAno())
                .placa(dto.getPlaca())
                .cor(dto.getCor())
                .cliente(cliente)
                .build();
        return veiculoRepository.save(veiculo);
    }

    // Atualiza um veículo existente
    @Transactional
    public Veiculo atualizar(Long id, VeiculoDTO dto) {
        Veiculo veiculo = buscarPorId(id);
        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com id: " + dto.getClienteId()));

        veiculo.setMarca(dto.getMarca());
        veiculo.setModelo(dto.getModelo());
        veiculo.setAno(dto.getAno());
        veiculo.setPlaca(dto.getPlaca());
        veiculo.setCor(dto.getCor());
        veiculo.setCliente(cliente);
        return veiculoRepository.save(veiculo);
    }

    // Exclui um veículo por ID
    @Transactional
    public void excluir(Long id) {
        Veiculo veiculo = buscarPorId(id);
        veiculoRepository.delete(veiculo);
    }
}

package com.estudos.oficinamecanicabackend.service;

import com.estudos.oficinamecanicabackend.dto.*;
import com.estudos.oficinamecanicabackend.entity.*;
import com.estudos.oficinamecanicabackend.enums.StatusOS;
import com.estudos.oficinamecanicabackend.exception.BusinessException;
import com.estudos.oficinamecanicabackend.exception.ResourceNotFoundException;
import com.estudos.oficinamecanicabackend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// ===========================================
// Service da entidade OrdemServico
// Contém as regras de negócio principais:
// - Abrir OS
// - Alterar status
// - Adicionar/remover serviços e peças
// - Calcular valor total automaticamente
// ===========================================
@Service
@RequiredArgsConstructor
public class OrdemServicoService {

    private final OrdemServicoRepository ordemServicoRepository;
    private final VeiculoRepository veiculoRepository;
    private final EquipeMecanicaRepository equipeMecanicaRepository;
    private final ServicoRepository servicoRepository;
    private final PecaRepository pecaRepository;
    private final OrdemServicoServicoRepository ordemServicoServicoRepository;
    private final OrdemServicoPecaRepository ordemServicoPecaRepository;

    // Lista todas as ordens de serviço
    @Transactional(readOnly = true)
    public List<OrdemServico> listarTodos() {
        return ordemServicoRepository.findAll();
    }

    // Busca uma OS por ID
    @Transactional(readOnly = true)
    public OrdemServico buscarPorId(Long id) {
        return ordemServicoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ordem de Serviço não encontrada com id: " + id));
    }

    // ===========================================
    // Abrir uma nova Ordem de Serviço
    // Regra: ao abrir, o status deve ser EM_ANALISE e valorTotal = 0
    // ===========================================
    @Transactional
    public OrdemServico abrirOrdemServico(OrdemServicoDTO dto) {
        Veiculo veiculo = veiculoRepository.findById(dto.getVeiculoId())
                .orElseThrow(() -> new ResourceNotFoundException("Veículo não encontrado com id: " + dto.getVeiculoId()));

        EquipeMecanica equipe = equipeMecanicaRepository.findById(dto.getEquipeId())
                .orElseThrow(() -> new ResourceNotFoundException("Equipe não encontrada com id: " + dto.getEquipeId()));

        OrdemServico os = OrdemServico.builder()
                .dataAbertura(LocalDate.now())
                .status(StatusOS.EM_ANALISE)
                .problemaRelatado(dto.getProblemaRelatado())
                .observacoes(dto.getObservacoes())
                .valorTotal(BigDecimal.ZERO)
                .veiculo(veiculo)
                .equipe(equipe)
                .servicos(new ArrayList<>())
                .pecas(new ArrayList<>())
                .build();

        return ordemServicoRepository.save(os);
    }

    // ===========================================
    // Alterar o status de uma OS
    // Regras de transição:
    //   EM_ANALISE -> EM_ANDAMENTO
    //   EM_ANDAMENTO -> CONCLUIDA ou CANCELADA
    //   EM_ANALISE -> CANCELADA
    //   CONCLUIDA e CANCELADA são status finais (não podem ser alterados)
    // ===========================================
    @Transactional
    public OrdemServico alterarStatus(Long id, StatusOSDTO dto) {
        OrdemServico os = buscarPorId(id);
        StatusOS novoStatus = dto.getStatus();

        // Valida as transições de status permitidas
        if (os.getStatus() == StatusOS.CONCLUIDA) {
            throw new BusinessException("Não é possível alterar o status de uma OS já Concluída");
        }
        if (os.getStatus() == StatusOS.CANCELADA) {
            throw new BusinessException("Não é possível alterar o status de uma OS já Cancelada");
        }
        if (os.getStatus() == StatusOS.EM_ANALISE && novoStatus == StatusOS.CONCLUIDA) {
            throw new BusinessException("Uma OS em Análise deve primeiro ir para Em Andamento antes de ser Concluída");
        }
        if (novoStatus == StatusOS.EM_ANALISE) {
            throw new BusinessException("Não é possível voltar o status para Em Análise");
        }

        os.setStatus(novoStatus);

        // Se concluída ou cancelada, registra a data de conclusão
        if (novoStatus == StatusOS.CONCLUIDA || novoStatus == StatusOS.CANCELADA) {
            os.setDataConclusao(LocalDate.now());
        }

        return ordemServicoRepository.save(os);
    }

    // ===========================================
    // Adicionar um serviço a uma OS
    // Recalcula o valor total automaticamente
    // ===========================================
    @Transactional
    public OrdemServico adicionarServico(Long osId, OrdemServicoServicoDTO dto) {
        OrdemServico os = buscarPorId(osId);

        if (os.getStatus() == StatusOS.CONCLUIDA || os.getStatus() == StatusOS.CANCELADA) {
            throw new BusinessException("Não é possível adicionar serviços a uma OS Concluída ou Cancelada");
        }

        Servico servico = servicoRepository.findById(dto.getServicoId())
                .orElseThrow(() -> new ResourceNotFoundException("Serviço não encontrado com id: " + dto.getServicoId()));

        OrdemServicoServico osServico = OrdemServicoServico.builder()
                .ordemServico(os)
                .servico(servico)
                .build();

        ordemServicoServicoRepository.save(osServico);

        // Recalcula o valor total
        os.getServicos().add(osServico);
        os.calcularValorTotal();
        return ordemServicoRepository.save(os);
    }

    // ===========================================
    // Remover um serviço de uma OS
    // Recalcula o valor total automaticamente
    // ===========================================
    @Transactional
    public OrdemServico removerServico(Long osId, Long servicoId) {
        OrdemServico os = buscarPorId(osId);

        if (os.getStatus() == StatusOS.CONCLUIDA || os.getStatus() == StatusOS.CANCELADA) {
            throw new BusinessException("Não é possível remover serviços de uma OS Concluída ou Cancelada");
        }

        OrdemServicoServico osServico = os.getServicos().stream()
                .filter(s -> s.getServico().getId().equals(servicoId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Serviço não encontrado nesta OS"));

        os.getServicos().remove(osServico);
        ordemServicoServicoRepository.delete(osServico);

        os.calcularValorTotal();
        return ordemServicoRepository.save(os);
    }

    // ===========================================
    // Adicionar uma peça a uma OS
    // Recalcula o valor total automaticamente
    // ===========================================
    @Transactional
    public OrdemServico adicionarPeca(Long osId, OrdemServicoPecaDTO dto) {
        OrdemServico os = buscarPorId(osId);

        if (os.getStatus() == StatusOS.CONCLUIDA || os.getStatus() == StatusOS.CANCELADA) {
            throw new BusinessException("Não é possível adicionar peças a uma OS Concluída ou Cancelada");
        }

        Peca peca = pecaRepository.findById(dto.getPecaId())
                .orElseThrow(() -> new ResourceNotFoundException("Peça não encontrada com id: " + dto.getPecaId()));

        // Verifica se há estoque suficiente
        if (peca.getQuantidadeEstoque() < dto.getQuantidade()) {
            throw new BusinessException("Estoque insuficiente para a peça " + peca.getNome()
                    + ". Disponível: " + peca.getQuantidadeEstoque() + ", Solicitado: " + dto.getQuantidade());
        }

        OrdemServicoPeca osPeca = OrdemServicoPeca.builder()
                .ordemServico(os)
                .peca(peca)
                .quantidade(dto.getQuantidade())
                .build();

        ordemServicoPecaRepository.save(osPeca);

        // Recalcula o valor total
        os.getPecas().add(osPeca);
        os.calcularValorTotal();
        return ordemServicoRepository.save(os);
    }

    // ===========================================
    // Remover uma peça de uma OS
    // Recalcula o valor total automaticamente
    // ===========================================
    @Transactional
    public OrdemServico removerPeca(Long osId, Long pecaId) {
        OrdemServico os = buscarPorId(osId);

        if (os.getStatus() == StatusOS.CONCLUIDA || os.getStatus() == StatusOS.CANCELADA) {
            throw new BusinessException("Não é possível remover peças de uma OS Concluída ou Cancelada");
        }

        OrdemServicoPeca osPeca = os.getPecas().stream()
                .filter(p -> p.getPeca().getId().equals(pecaId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Peça não encontrada nesta OS"));

        os.getPecas().remove(osPeca);
        ordemServicoPecaRepository.delete(osPeca);

        os.calcularValorTotal();
        return ordemServicoRepository.save(os);
    }

    // Exclui uma OS por ID
    @Transactional
    public void excluir(Long id) {
        OrdemServico os = buscarPorId(id);
        ordemServicoRepository.delete(os);
    }

    // ===========================================
    // Converte uma OrdemServico para o DTO de resposta
    // ===========================================
    public OrdemServicoResponseDTO paraResponseDTO(OrdemServico os) {
        List<OrdemServicoResponseDTO.ItemServicoResponseDTO> servicos = os.getServicos().stream()
                .map(s -> OrdemServicoResponseDTO.ItemServicoResponseDTO.builder()
                        .servicoId(s.getServico().getId())
                        .descricao(s.getServico().getDescricao())
                        .valorMaoDeObra(s.getServico().getValorMaoDeObra())
                        .build())
                .collect(Collectors.toList());

        List<OrdemServicoResponseDTO.ItemPecaResponseDTO> pecas = os.getPecas().stream()
                .map(p -> OrdemServicoResponseDTO.ItemPecaResponseDTO.builder()
                        .pecaId(p.getPeca().getId())
                        .pecaNome(p.getPeca().getNome())
                        .quantidade(p.getQuantidade())
                        .valorUnitario(p.getPeca().getValorUnitario())
                        .subtotal(p.getPeca().getValorUnitario().multiply(BigDecimal.valueOf(p.getQuantidade())))
                        .build())
                .collect(Collectors.toList());

        return OrdemServicoResponseDTO.builder()
                .id(os.getId())
                .dataAbertura(os.getDataAbertura())
                .dataConclusao(os.getDataConclusao())
                .status(os.getStatus())
                .observacoes(os.getObservacoes())
                .valorTotal(os.getValorTotal())
                .problemaRelatado(os.getProblemaRelatado())
                .veiculoId(os.getVeiculo().getId())
                .veiculoPlaca(os.getVeiculo().getPlaca())
                .equipeId(os.getEquipe().getId())
                .equipeNome(os.getEquipe().getNome())
                .servicos(servicos)
                .pecas(pecas)
                .build();
    }
}

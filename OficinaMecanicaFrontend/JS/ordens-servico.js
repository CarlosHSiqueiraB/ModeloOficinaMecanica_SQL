// ==========================================
// Ordens de Serviço - CRUD + Ações
// ==========================================

let ordensData = [];

async function loadOrdensServico() {
    try {
        ordensData = await api.get(ENDPOINTS.ordensServico);
        await Promise.all([
            populateSelect('os-veiculo', ENDPOINTS.veiculos, 'placa'),
            populateSelect('os-equipe', ENDPOINTS.equipes, 'nome')
        ]);
        renderOrdensTable();
    } catch (error) {
        showToast('Erro ao carregar ordens de serviço', 'error');
        console.error(error);
    }
}

function renderOrdensTable() {
    const tbody = document.getElementById('ordens-table');
    if (ordensData.length === 0) {
        tbody.innerHTML = '<tr><td colspan="7" class="py-8 text-center text-gray-400"><i class="fas fa-clipboard-list text-4xl mb-2 block opacity-30"></i>Nenhuma ordem de serviço encontrada</td></tr>';
        return;
    }
    tbody.innerHTML = ordensData.map(os => `
        <tr>
            <td class="py-3 px-4 text-sm font-medium">#${os.id}</td>
            <td class="py-3 px-4 text-sm">${formatDate(os.dataAbertura)}</td>
            <td class="py-3 px-4 text-sm">${os.veiculoPlaca || '-'}</td>
            <td class="py-3 px-4 text-sm">${os.equipeNome || '-'}</td>
            <td class="py-3 px-4 text-sm">
                <span class="status-badge ${getStatusClass(os.status)}">${getStatusLabel(os.status)}</span>
            </td>
            <td class="py-3 px-4 text-sm font-semibold">${formatCurrency(os.valorTotal || 0)}</td>
            <td class="py-3 px-4 text-sm">
                <div class="flex gap-2 flex-wrap">
                    <button onclick="viewOrdemServico(${os.id})" class="btn-action btn-view" title="Ver detalhes"><i class="fas fa-eye"></i></button>
                    ${os.status !== 'CONCLUIDA' && os.status !== 'CANCELADA' ? `
                        <button onclick="showStatusOptions(${os.id})" class="btn-action btn-edit" title="Alterar status"><i class="fas fa-sync"></i></button>
                        <button onclick="deleteOrdemServico(${os.id})" class="btn-action btn-delete" title="Excluir"><i class="fas fa-trash"></i></button>
                    ` : ''}
                </div>
            </td>
        </tr>
    `).join('');
}

// ==========================================
// Criar Ordem de Serviço
// ==========================================

document.getElementById('os-form').addEventListener('submit', async (e) => {
    e.preventDefault();
    const data = {
        veiculoId: parseInt(document.getElementById('os-veiculo').value),
        equipeId: parseInt(document.getElementById('os-equipe').value),
        problemaRelatado: document.getElementById('os-problema').value,
        observacoes: document.getElementById('os-observacoes').value
    };

    try {
        await api.post(ENDPOINTS.ordensServico, data);
        showToast('Ordem de serviço aberta com sucesso!', 'success');
        closeModal('os-modal');
        loadOrdensServico();
    } catch (error) {
        showToast(error.message || 'Erro ao abrir ordem de serviço', 'error');
    }
});

// ==========================================
// Excluir Ordem de Serviço
// ==========================================

async function deleteOrdemServico(id) {
    if (!confirm('Tem certeza que deseja excluir esta ordem de serviço?')) return;
    try {
        await api.delete(`${ENDPOINTS.ordensServico}/${id}`);
        showToast('Ordem de serviço excluída com sucesso!', 'success');
        loadOrdensServico();
    } catch (error) {
        showToast(error.message || 'Erro ao excluir ordem de serviço', 'error');
    }
}

// ==========================================
// Alterar Status
// ==========================================

function showStatusOptions(osId) {
    const statusOptions = ['EM_ANALISE', 'EM_ANDAMENTO', 'CONCLUIDA', 'CANCELADA'];
    const statusLabels = {
        'EM_ANALISE': 'Em Análise',
        'EM_ANDAMENTO': 'Em Andamento',
        'CONCLUIDA': 'Concluída',
        'CANCELADA': 'Cancelada'
    };

    const modal = document.createElement('div');
    modal.className = 'modal fixed inset-0 bg-black bg-opacity-50 z-50 flex items-center justify-center';
    modal.id = 'status-modal';
    modal.innerHTML = `
        <div class="bg-white rounded-lg shadow-xl w-full max-w-sm mx-4">
            <div class="flex items-center justify-between p-4 border-b">
                <h3 class="text-lg font-semibold">Alterar Status - OS #${osId}</h3>
                <button onclick="document.getElementById('status-modal').remove()" class="text-gray-400 hover:text-gray-600">
                    <i class="fas fa-times text-xl"></i>
                </button>
            </div>
            <div class="p-4 space-y-2">
                ${statusOptions.map(status => `
                    <button onclick="changeStatus(${osId}, '${status}')" class="w-full text-left px-4 py-3 rounded-lg border hover:bg-gray-50 transition-colors flex items-center gap-3">
                        <span class="status-badge ${getStatusClass(status)}">${statusLabels[status]}</span>
                    </button>
                `).join('')}
            </div>
        </div>
    `;
    document.body.appendChild(modal);
}

async function changeStatus(osId, status) {
    try {
        await api.patch(`${ENDPOINTS.ordensServico}/${osId}/status`, { status });
        showToast('Status alterado com sucesso!', 'success');
        document.getElementById('status-modal').remove();
        loadOrdensServico();
    } catch (error) {
        showToast(error.message || 'Erro ao alterar status', 'error');
    }
}

// ==========================================
// Detalhes da OS
// ==========================================

async function viewOrdemServico(id) {
    try {
        const os = await api.get(`${ENDPOINTS.ordensServico}/${id}`);
        const servicos = await api.get(ENDPOINTS.servicos);
        const pecas = await api.get(ENDPOINTS.pecas);

        const content = document.getElementById('os-detalhe-content');
        content.innerHTML = `
            <div class="grid grid-cols-2 gap-4 mb-6">
                <div>
                    <p class="text-sm text-gray-500">ID</p>
                    <p class="font-semibold">#${os.id}</p>
                </div>
                <div>
                    <p class="text-sm text-gray-500">Status</p>
                    <span class="status-badge ${getStatusClass(os.status)}">${getStatusLabel(os.status)}</span>
                </div>
                <div>
                    <p class="text-sm text-gray-500">Data Abertura</p>
                    <p class="font-semibold">${formatDate(os.dataAbertura)}</p>
                </div>
                <div>
                    <p class="text-sm text-gray-500">Data Conclusão</p>
                    <p class="font-semibold">${os.dataConclusao ? formatDate(os.dataConclusao) : '-'}</p>
                </div>
                <div>
                    <p class="text-sm text-gray-500">Veículo</p>
                    <p class="font-semibold">${os.veiculoPlaca || '-'}</p>
                </div>
                <div>
                    <p class="text-sm text-gray-500">Equipe</p>
                    <p class="font-semibold">${os.equipeNome || '-'}</p>
                </div>
                <div class="col-span-2">
                    <p class="text-sm text-gray-500">Problema Relatado</p>
                    <p class="font-semibold">${os.problemaRelatado || '-'}</p>
                </div>
                <div class="col-span-2">
                    <p class="text-sm text-gray-500">Observações</p>
                    <p>${os.observacoes || '-'}</p>
                </div>
            </div>

            <div class="border-t pt-4 mb-4">
                <div class="flex items-center justify-between mb-3">
                    <h4 class="font-semibold text-gray-700">Serviços</h4>
                    ${os.status !== 'CONCLUIDA' && os.status !== 'CANCELADA' ? `
                        <select id="add-servico-select" class="border rounded px-2 py-1 text-sm">
                            <option value="">Adicionar serviço...</option>
                            ${servicos.map(s => `<option value="${s.id}">${s.descricao} - ${formatCurrency(s.valorMaoDeObra)}</option>`).join('')}
                        </select>
                        <button onclick="addServicoToOs(${os.id})" class="text-sm bg-blue-500 text-white px-3 py-1 rounded hover:bg-blue-600">Adicionar</button>
                    ` : ''}
                </div>
                ${os.servicos && os.servicos.length > 0 ? `
                    <ul class="space-y-2">
                        ${os.servicos.map(s => `
                            <li class="flex items-center justify-between bg-gray-50 px-3 py-2 rounded">
                                <span class="text-sm">${s.descricao} (${formatCurrency(s.valorMaoDeObra)})</span>
                                ${os.status !== 'CONCLUIDA' && os.status !== 'CANCELADA' ? `
                                    <button onclick="removeServicoFromOs(${os.id}, ${s.servicoId})" class="text-red-500 hover:text-red-700 text-sm">
                                        <i class="fas fa-times"></i>
                                    </button>
                                ` : ''}
                            </li>
                        `).join('')}
                    </ul>
                ` : '<p class="text-gray-400 text-sm">Nenhum serviço adicionado</p>'}
            </div>

            <div class="border-t pt-4 mb-4">
                <div class="flex items-center justify-between mb-3">
                    <h4 class="font-semibold text-gray-700">Peças</h4>
                    ${os.status !== 'CONCLUIDA' && os.status !== 'CANCELADA' ? `
                        <div class="flex gap-2">
                            <select id="add-peca-select" class="border rounded px-2 py-1 text-sm">
                                <option value="">Adicionar peça...</option>
                                ${pecas.map(p => `<option value="${p.id}">${p.nome} - ${formatCurrency(p.valorUnitario)} (Est: ${p.quantidadeEstoque})</option>`).join('')}
                            </select>
                            <input type="number" id="add-peca-qty" min="1" value="1" class="border rounded px-2 py-1 text-sm w-16">
                            <button onclick="addPecaToOs(${os.id})" class="text-sm bg-green-500 text-white px-3 py-1 rounded hover:bg-green-600">Adicionar</button>
                        </div>
                    ` : ''}
                </div>
                ${os.pecas && os.pecas.length > 0 ? `
                    <table class="w-full text-sm">
                        <thead>
                            <tr class="border-b">
                                <th class="text-left py-2">Peça</th>
                                <th class="text-right py-2">Qtd</th>
                                <th class="text-right py-2">Valor Unit.</th>
                                <th class="text-right py-2">Subtotal</th>
                                ${os.status !== 'CONCLUIDA' && os.status !== 'CANCELADA' ? '<th class="py-2"></th>' : ''}
                            </tr>
                        </thead>
                        <tbody>
                            ${os.pecas.map(p => `
                                <tr class="border-b">
                                    <td class="py-2">${p.pecaNome}</td>
                                    <td class="text-right py-2">${p.quantidade}</td>
                                    <td class="text-right py-2">${formatCurrency(p.valorUnitario)}</td>
                                    <td class="text-right py-2 font-semibold">${formatCurrency(p.subtotal)}</td>
                                    ${os.status !== 'CONCLUIDA' && os.status !== 'CANCELADA' ? `
                                        <td class="text-right py-2">
                                            <button onclick="removePecaFromOs(${os.id}, ${p.pecaId})" class="text-red-500 hover:text-red-700">
                                                <i class="fas fa-times"></i>
                                            </button>
                                        </td>
                                    ` : ''}
                                </tr>
                            `).join('')}
                        </tbody>
                    </table>
                ` : '<p class="text-gray-400 text-sm">Nenhuma peça adicionada</p>'}
            </div>

            <div class="border-t pt-4">
                <div class="flex justify-between items-center">
                    <span class="font-semibold text-gray-700">Valor Total:</span>
                    <span class="text-xl font-bold text-primary">${formatCurrency(os.valorTotal || 0)}</span>
                </div>
            </div>
        `;

        openModal('os-detalhe-modal');
    } catch (error) {
        showToast('Erro ao carregar detalhes da OS', 'error');
    }
}

// ==========================================
// Adicionar Serviço à OS
// ==========================================

async function addServicoToOs(osId) {
    const select = document.getElementById('add-servico-select');
    const servicoId = select.value;
    if (!servicoId) {
        showToast('Selecione um serviço', 'info');
        return;
    }

    try {
        await api.post(`${ENDPOINTS.ordensServico}/${osId}/servicos`, { servicoId: parseInt(servicoId) });
        showToast('Serviço adicionado com sucesso!', 'success');
        viewOrdemServico(osId);
        loadOrdensServico();
    } catch (error) {
        showToast(error.message || 'Erro ao adicionar serviço', 'error');
    }
}

// ==========================================
// Remover Serviço da OS
// ==========================================

async function removeServicoFromOs(osId, servicoId) {
    if (!confirm('Tem certeza que deseja remover este serviço?')) return;
    try {
        await api.delete(`${ENDPOINTS.ordensServico}/${osId}/servicos/${servicoId}`);
        showToast('Serviço removido com sucesso!', 'success');
        viewOrdemServico(osId);
        loadOrdensServico();
    } catch (error) {
        showToast(error.message || 'Erro ao remover serviço', 'error');
    }
}

// ==========================================
// Adicionar Peça à OS
// ==========================================

async function addPecaToOs(osId) {
    const select = document.getElementById('add-peca-select');
    const qtyInput = document.getElementById('add-peca-qty');
    const pecaId = select.value;
    const quantidade = parseInt(qtyInput.value);

    if (!pecaId) {
        showToast('Selecione uma peça', 'info');
        return;
    }

    try {
        await api.post(`${ENDPOINTS.ordensServico}/${osId}/pecas`, {
            pecaId: parseInt(pecaId),
            quantidade
        });
        showToast('Peça adicionada com sucesso!', 'success');
        viewOrdemServico(osId);
        loadOrdensServico();
    } catch (error) {
        showToast(error.message || 'Erro ao adicionar peça', 'error');
    }
}

// ==========================================
// Remover Peça da OS
// ==========================================

async function removePecaFromOs(osId, pecaId) {
    if (!confirm('Tem certeza que deseja remover esta peça?')) return;
    try {
        await api.delete(`${ENDPOINTS.ordensServico}/${osId}/pecas/${pecaId}`);
        showToast('Peça removida com sucesso!', 'success');
        viewOrdemServico(osId);
        loadOrdensServico();
    } catch (error) {
        showToast(error.message || 'Erro ao remover peça', 'error');
    }
}

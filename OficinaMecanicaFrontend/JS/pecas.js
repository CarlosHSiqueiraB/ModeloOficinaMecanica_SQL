// ==========================================
// Peças - CRUD
// ==========================================

let pecasData = [];

async function loadPecas() {
    try {
        pecasData = await api.get(ENDPOINTS.pecas);
        renderPecasTable();
    } catch (error) {
        showToast('Erro ao carregar peças', 'error');
        console.error(error);
    }
}

function renderPecasTable() {
    const tbody = document.getElementById('pecas-table');
    if (pecasData.length === 0) {
        tbody.innerHTML = '<tr><td colspan="6" class="py-8 text-center text-gray-400"><i class="fas fa-cogs text-4xl mb-2 block opacity-30"></i>Nenhuma peça cadastrada</td></tr>';
        return;
    }
    tbody.innerHTML = pecasData.map(p => `
        <tr>
            <td class="py-3 px-4 text-sm font-medium">${p.id}</td>
            <td class="py-3 px-4 text-sm font-semibold">${p.nome}</td>
            <td class="py-3 px-4 text-sm">${p.descricao || '-'}</td>
            <td class="py-3 px-4 text-sm">${formatCurrency(p.valorUnitario)}</td>
            <td class="py-3 px-4 text-sm">
                <span class="${p.quantidadeEstoque <= 5 ? 'text-red-600 font-bold' : ''}">${p.quantidadeEstoque}</span>
            </td>
            <td class="py-3 px-4 text-sm">
                <div class="flex gap-2">
                    <button onclick="editPeca(${p.id})" class="btn-action btn-edit"><i class="fas fa-edit"></i></button>
                    <button onclick="deletePeca(${p.id})" class="btn-action btn-delete"><i class="fas fa-trash"></i></button>
                </div>
            </td>
        </tr>
    `).join('');
}

document.getElementById('peca-form').addEventListener('submit', async (e) => {
    e.preventDefault();
    const id = document.getElementById('peca-id').value;
    const data = {
        nome: document.getElementById('peca-nome').value,
        descricao: document.getElementById('peca-descricao').value,
        valorUnitario: parseFloat(document.getElementById('peca-valor').value),
        quantidadeEstoque: parseInt(document.getElementById('peca-estoque').value)
    };

    try {
        if (id) {
            await api.put(`${ENDPOINTS.pecas}/${id}`, data);
            showToast('Peça atualizada com sucesso!', 'success');
        } else {
            await api.post(ENDPOINTS.pecas, data);
            showToast('Peça cadastrada com sucesso!', 'success');
        }
        closeModal('peca-modal');
        loadPecas();
    } catch (error) {
        showToast(error.message || 'Erro ao salvar peça', 'error');
    }
});

async function editPeca(id) {
    try {
        const peca = await api.get(`${ENDPOINTS.pecas}/${id}`);
        document.getElementById('peca-id').value = peca.id;
        document.getElementById('peca-nome').value = peca.nome;
        document.getElementById('peca-descricao').value = peca.descricao || '';
        document.getElementById('peca-valor').value = peca.valorUnitario;
        document.getElementById('peca-estoque').value = peca.quantidadeEstoque;
        document.getElementById('peca-modal-title').textContent = 'Editar Peça';
        openModal('peca-modal');
    } catch (error) {
        showToast('Erro ao carregar dados da peça', 'error');
    }
}

async function deletePeca(id) {
    if (!confirm('Tem certeza que deseja excluir esta peça?')) return;
    try {
        await api.delete(`${ENDPOINTS.pecas}/${id}`);
        showToast('Peça excluída com sucesso!', 'success');
        loadPecas();
    } catch (error) {
        showToast(error.message || 'Erro ao excluir peça', 'error');
    }
}

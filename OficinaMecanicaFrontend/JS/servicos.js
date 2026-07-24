// ==========================================
// Serviços - CRUD
// ==========================================

let servicosData = [];

async function loadServicos() {
    try {
        servicosData = await api.get(ENDPOINTS.servicos);
        renderServicosTable();
    } catch (error) {
        showToast('Erro ao carregar serviços', 'error');
        console.error(error);
    }
}

function renderServicosTable() {
    const tbody = document.getElementById('servicos-table');
    if (servicosData.length === 0) {
        tbody.innerHTML = '<tr><td colspan="4" class="py-8 text-center text-gray-400"><i class="fas fa-screwdriver-wrench text-4xl mb-2 block opacity-30"></i>Nenhum serviço cadastrado</td></tr>';
        return;
    }
    tbody.innerHTML = servicosData.map(s => `
        <tr>
            <td class="py-3 px-4 text-sm font-medium">${s.id}</td>
            <td class="py-3 px-4 text-sm">${s.descricao}</td>
            <td class="py-3 px-4 text-sm font-semibold">${formatCurrency(s.valorMaoDeObra)}</td>
            <td class="py-3 px-4 text-sm">
                <div class="flex gap-2">
                    <button onclick="editServico(${s.id})" class="btn-action btn-edit"><i class="fas fa-edit"></i></button>
                    <button onclick="deleteServico(${s.id})" class="btn-action btn-delete"><i class="fas fa-trash"></i></button>
                </div>
            </td>
        </tr>
    `).join('');
}

document.getElementById('servico-form').addEventListener('submit', async (e) => {
    e.preventDefault();
    const id = document.getElementById('servico-id').value;
    const data = {
        descricao: document.getElementById('servico-descricao').value,
        valorMaoDeObra: parseFloat(document.getElementById('servico-valor').value)
    };

    try {
        if (id) {
            await api.put(`${ENDPOINTS.servicos}/${id}`, data);
            showToast('Serviço atualizado com sucesso!', 'success');
        } else {
            await api.post(ENDPOINTS.servicos, data);
            showToast('Serviço cadastrado com sucesso!', 'success');
        }
        closeModal('servico-modal');
        loadServicos();
    } catch (error) {
        showToast(error.message || 'Erro ao salvar serviço', 'error');
    }
});

async function editServico(id) {
    try {
        const servico = await api.get(`${ENDPOINTS.servicos}/${id}`);
        document.getElementById('servico-id').value = servico.id;
        document.getElementById('servico-descricao').value = servico.descricao;
        document.getElementById('servico-valor').value = servico.valorMaoDeObra;
        document.getElementById('servico-modal-title').textContent = 'Editar Serviço';
        openModal('servico-modal');
    } catch (error) {
        showToast('Erro ao carregar dados do serviço', 'error');
    }
}

async function deleteServico(id) {
    if (!confirm('Tem certeza que deseja excluir este serviço?')) return;
    try {
        await api.delete(`${ENDPOINTS.servicos}/${id}`);
        showToast('Serviço excluído com sucesso!', 'success');
        loadServicos();
    } catch (error) {
        showToast(error.message || 'Erro ao excluir serviço', 'error');
    }
}

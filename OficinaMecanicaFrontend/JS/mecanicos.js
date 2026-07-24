// ==========================================
// Mecânicos - CRUD
// ==========================================

let mecanicosData = [];

async function loadMecanicos() {
    try {
        mecanicosData = await api.get(ENDPOINTS.mecanicos);
        renderMecanicosTable();
    } catch (error) {
        showToast('Erro ao carregar mecânicos', 'error');
        console.error(error);
    }
}

function renderMecanicosTable() {
    const tbody = document.getElementById('mecanicos-table');
    if (mecanicosData.length === 0) {
        tbody.innerHTML = '<tr><td colspan="6" class="py-8 text-center text-gray-400"><i class="fas fa-user-gear text-4xl mb-2 block opacity-30"></i>Nenhum mecânico cadastrado</td></tr>';
        return;
    }
    tbody.innerHTML = mecanicosData.map(m => `
        <tr>
            <td class="py-3 px-4 text-sm font-medium">${m.id}</td>
            <td class="py-3 px-4 text-sm">${m.nome}</td>
            <td class="py-3 px-4 text-sm">${m.cpf}</td>
            <td class="py-3 px-4 text-sm">${m.especialidade || '-'}</td>
            <td class="py-3 px-4 text-sm">${m.telefone || '-'}</td>
            <td class="py-3 px-4 text-sm">
                <div class="flex gap-2">
                    <button onclick="editMecanico(${m.id})" class="btn-action btn-edit"><i class="fas fa-edit"></i></button>
                    <button onclick="deleteMecanico(${m.id})" class="btn-action btn-delete"><i class="fas fa-trash"></i></button>
                </div>
            </td>
        </tr>
    `).join('');
}

document.getElementById('mecanico-form').addEventListener('submit', async (e) => {
    e.preventDefault();
    const id = document.getElementById('mecanico-id').value;
    const data = {
        nome: document.getElementById('mecanico-nome').value,
        cpf: document.getElementById('mecanico-cpf').value,
        especialidade: document.getElementById('mecanico-especialidade').value,
        telefone: document.getElementById('mecanico-telefone').value
    };

    try {
        if (id) {
            await api.put(`${ENDPOINTS.mecanicos}/${id}`, data);
            showToast('Mecânico atualizado com sucesso!', 'success');
        } else {
            await api.post(ENDPOINTS.mecanicos, data);
            showToast('Mecânico cadastrado com sucesso!', 'success');
        }
        closeModal('mecanico-modal');
        loadMecanicos();
    } catch (error) {
        showToast(error.message || 'Erro ao salvar mecânico', 'error');
    }
});

async function editMecanico(id) {
    try {
        const mecanico = await api.get(`${ENDPOINTS.mecanicos}/${id}`);
        document.getElementById('mecanico-id').value = mecanico.id;
        document.getElementById('mecanico-nome').value = mecanico.nome;
        document.getElementById('mecanico-cpf').value = mecanico.cpf;
        document.getElementById('mecanico-especialidade').value = mecanico.especialidade || '';
        document.getElementById('mecanico-telefone').value = mecanico.telefone || '';
        document.getElementById('mecanico-modal-title').textContent = 'Editar Mecânico';
        openModal('mecanico-modal');
    } catch (error) {
        showToast('Erro ao carregar dados do mecânico', 'error');
    }
}

async function deleteMecanico(id) {
    if (!confirm('Tem certeza que deseja excluir este mecânico?')) return;
    try {
        await api.delete(`${ENDPOINTS.mecanicos}/${id}`);
        showToast('Mecânico excluído com sucesso!', 'success');
        loadMecanicos();
    } catch (error) {
        showToast(error.message || 'Erro ao excluir mecânico', 'error');
    }
}

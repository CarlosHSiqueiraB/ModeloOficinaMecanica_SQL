// ==========================================
// Equipes Mecânicas - CRUD
// ==========================================

let equipesData = [];

async function loadEquipes() {
    try {
        equipesData = await api.get(ENDPOINTS.equipes);
        await populateSelect('equipe-mecanico', ENDPOINTS.mecanicos, 'nome');
        renderEquipesTable();
    } catch (error) {
        showToast('Erro ao carregar equipes', 'error');
        console.error(error);
    }
}

function renderEquipesTable() {
    const tbody = document.getElementById('equipes-table');
    if (equipesData.length === 0) {
        tbody.innerHTML = '<tr><td colspan="4" class="py-8 text-center text-gray-400"><i class="fas fa-people-group text-4xl mb-2 block opacity-30"></i>Nenhuma equipe cadastrada</td></tr>';
        return;
    }
    tbody.innerHTML = equipesData.map(e => `
        <tr>
            <td class="py-3 px-4 text-sm font-medium">${e.id}</td>
            <td class="py-3 px-4 text-sm">${e.nome}</td>
            <td class="py-3 px-4 text-sm">${e.mecanico ? e.mecanico.nome : '-'}</td>
            <td class="py-3 px-4 text-sm">
                <div class="flex gap-2">
                    <button onclick="editEquipe(${e.id})" class="btn-action btn-edit"><i class="fas fa-edit"></i></button>
                    <button onclick="deleteEquipe(${e.id})" class="btn-action btn-delete"><i class="fas fa-trash"></i></button>
                </div>
            </td>
        </tr>
    `).join('');
}

document.getElementById('equipe-form').addEventListener('submit', async (e) => {
    e.preventDefault();
    const id = document.getElementById('equipe-id').value;
    const data = {
        nome: document.getElementById('equipe-nome').value,
        mecanicoId: parseInt(document.getElementById('equipe-mecanico').value)
    };

    try {
        if (id) {
            await api.put(`${ENDPOINTS.equipes}/${id}`, data);
            showToast('Equipe atualizada com sucesso!', 'success');
        } else {
            await api.post(ENDPOINTS.equipes, data);
            showToast('Equipe cadastrada com sucesso!', 'success');
        }
        closeModal('equipe-modal');
        loadEquipes();
    } catch (error) {
        showToast(error.message || 'Erro ao salvar equipe', 'error');
    }
});

async function editEquipe(id) {
    try {
        const equipe = await api.get(`${ENDPOINTS.equipes}/${id}`);
        document.getElementById('equipe-id').value = equipe.id;
        document.getElementById('equipe-nome').value = equipe.nome;
        document.getElementById('equipe-mecanico').value = equipe.mecanico ? equipe.mecanico.id : '';
        document.getElementById('equipe-modal-title').textContent = 'Editar Equipe';
        openModal('equipe-modal');
    } catch (error) {
        showToast('Erro ao carregar dados da equipe', 'error');
    }
}

async function deleteEquipe(id) {
    if (!confirm('Tem certeza que deseja excluir esta equipe?')) return;
    try {
        await api.delete(`${ENDPOINTS.equipes}/${id}`);
        showToast('Equipe excluída com sucesso!', 'success');
        loadEquipes();
    } catch (error) {
        showToast(error.message || 'Erro ao excluir equipe', 'error');
    }
}

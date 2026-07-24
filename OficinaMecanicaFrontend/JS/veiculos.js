// ==========================================
// Veículos - CRUD
// ==========================================

let veiculosData = [];

async function loadVeiculos() {
    try {
        veiculosData = await api.get(ENDPOINTS.veiculos);
        await populateSelect('veiculo-cliente', ENDPOINTS.clientes, 'nome');
        renderVeiculosTable();
    } catch (error) {
        showToast('Erro ao carregar veículos', 'error');
        console.error(error);
    }
}

function renderVeiculosTable() {
    const tbody = document.getElementById('veiculos-table');
    if (veiculosData.length === 0) {
        tbody.innerHTML = '<tr><td colspan="8" class="py-8 text-center text-gray-400"><i class="fas fa-car text-4xl mb-2 block opacity-30"></i>Nenhum veículo cadastrado</td></tr>';
        return;
    }
    tbody.innerHTML = veiculosData.map(v => `
        <tr>
            <td class="py-3 px-4 text-sm font-medium">${v.id}</td>
            <td class="py-3 px-4 text-sm">${v.marca}</td>
            <td class="py-3 px-4 text-sm">${v.modelo}</td>
            <td class="py-3 px-4 text-sm">${v.ano}</td>
            <td class="py-3 px-4 text-sm font-mono font-semibold">${v.placa}</td>
            <td class="py-3 px-4 text-sm">${v.cor || '-'}</td>
            <td class="py-3 px-4 text-sm">${v.cliente ? v.cliente.nome : '-'}</td>
            <td class="py-3 px-4 text-sm">
                <div class="flex gap-2">
                    <button onclick="editVeiculo(${v.id})" class="btn-action btn-edit"><i class="fas fa-edit"></i></button>
                    <button onclick="deleteVeiculo(${v.id})" class="btn-action btn-delete"><i class="fas fa-trash"></i></button>
                </div>
            </td>
        </tr>
    `).join('');
}

document.getElementById('veiculo-form').addEventListener('submit', async (e) => {
    e.preventDefault();
    const id = document.getElementById('veiculo-id').value;
    const data = {
        marca: document.getElementById('veiculo-marca').value,
        modelo: document.getElementById('veiculo-modelo').value,
        ano: parseInt(document.getElementById('veiculo-ano').value),
        placa: document.getElementById('veiculo-placa').value.toUpperCase(),
        cor: document.getElementById('veiculo-cor').value,
        clienteId: parseInt(document.getElementById('veiculo-cliente').value)
    };

    try {
        if (id) {
            await api.put(`${ENDPOINTS.veiculos}/${id}`, data);
            showToast('Veículo atualizado com sucesso!', 'success');
        } else {
            await api.post(ENDPOINTS.veiculos, data);
            showToast('Veículo cadastrado com sucesso!', 'success');
        }
        closeModal('veiculo-modal');
        loadVeiculos();
    } catch (error) {
        showToast(error.message || 'Erro ao salvar veículo', 'error');
    }
});

async function editVeiculo(id) {
    try {
        const veiculo = await api.get(`${ENDPOINTS.veiculos}/${id}`);
        document.getElementById('veiculo-id').value = veiculo.id;
        document.getElementById('veiculo-marca').value = veiculo.marca;
        document.getElementById('veiculo-modelo').value = veiculo.modelo;
        document.getElementById('veiculo-ano').value = veiculo.ano;
        document.getElementById('veiculo-placa').value = veiculo.placa;
        document.getElementById('veiculo-cor').value = veiculo.cor || '';
        document.getElementById('veiculo-cliente').value = veiculo.cliente ? veiculo.cliente.id : '';
        document.getElementById('veiculo-modal-title').textContent = 'Editar Veículo';
        openModal('veiculo-modal');
    } catch (error) {
        showToast('Erro ao carregar dados do veículo', 'error');
    }
}

async function deleteVeiculo(id) {
    if (!confirm('Tem certeza que deseja excluir este veículo?')) return;
    try {
        await api.delete(`${ENDPOINTS.veiculos}/${id}`);
        showToast('Veículo excluído com sucesso!', 'success');
        loadVeiculos();
    } catch (error) {
        showToast(error.message || 'Erro ao excluir veículo', 'error');
    }
}

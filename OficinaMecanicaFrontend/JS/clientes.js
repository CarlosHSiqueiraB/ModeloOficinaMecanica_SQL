// ==========================================
// Clientes - CRUD
// ==========================================

let clientesData = [];

async function loadClientes() {
    try {
        clientesData = await api.get(ENDPOINTS.clientes);
        renderClientesTable();
    } catch (error) {
        showToast('Erro ao carregar clientes', 'error');
        console.error(error);
    }
}

function renderClientesTable() {
    const tbody = document.getElementById('clientes-table');
    if (clientesData.length === 0) {
        tbody.innerHTML = '<tr><td colspan="6" class="py-8 text-center text-gray-400"><i class="fas fa-users text-4xl mb-2 block opacity-30"></i>Nenhum cliente cadastrado</td></tr>';
        return;
    }
    tbody.innerHTML = clientesData.map(c => `
        <tr>
            <td class="py-3 px-4 text-sm font-medium">${c.id}</td>
            <td class="py-3 px-4 text-sm">${c.nome}</td>
            <td class="py-3 px-4 text-sm">${c.cpf}</td>
            <td class="py-3 px-4 text-sm">${c.telefone || '-'}</td>
            <td class="py-3 px-4 text-sm">${c.email || '-'}</td>
            <td class="py-3 px-4 text-sm">
                <div class="flex gap-2">
                    <button onclick="editCliente(${c.id})" class="btn-action btn-edit"><i class="fas fa-edit"></i></button>
                    <button onclick="deleteCliente(${c.id})" class="btn-action btn-delete"><i class="fas fa-trash"></i></button>
                </div>
            </td>
        </tr>
    `).join('');
}

// ==========================================
// Form Submit
// ==========================================

document.getElementById('cliente-form').addEventListener('submit', async (e) => {
    e.preventDefault();
    const id = document.getElementById('cliente-id').value;
    const data = {
        nome: document.getElementById('cliente-nome').value,
        cpf: document.getElementById('cliente-cpf').value,
        telefone: document.getElementById('cliente-telefone').value,
        email: document.getElementById('cliente-email').value,
        endereco: document.getElementById('cliente-endereco').value
    };

    try {
        if (id) {
            await api.put(`${ENDPOINTS.clientes}/${id}`, data);
            showToast('Cliente atualizado com sucesso!', 'success');
        } else {
            await api.post(ENDPOINTS.clientes, data);
            showToast('Cliente cadastrado com sucesso!', 'success');
        }
        closeModal('cliente-modal');
        loadClientes();
    } catch (error) {
        showToast(error.message || 'Erro ao salvar cliente', 'error');
    }
});

// ==========================================
// Edit
// ==========================================

async function editCliente(id) {
    try {
        const cliente = await api.get(`${ENDPOINTS.clientes}/${id}`);
        document.getElementById('cliente-id').value = cliente.id;
        document.getElementById('cliente-nome').value = cliente.nome;
        document.getElementById('cliente-cpf').value = cliente.cpf;
        document.getElementById('cliente-telefone').value = cliente.telefone || '';
        document.getElementById('cliente-email').value = cliente.email || '';
        document.getElementById('cliente-endereco').value = cliente.endereco || '';
        document.getElementById('cliente-modal-title').textContent = 'Editar Cliente';
        openModal('cliente-modal');
    } catch (error) {
        showToast('Erro ao carregar dados do cliente', 'error');
    }
}

// ==========================================
// Delete
// ==========================================

async function deleteCliente(id) {
    if (!confirm('Tem certeza que deseja excluir este cliente?')) return;
    try {
        await api.delete(`${ENDPOINTS.clientes}/${id}`);
        showToast('Cliente excluído com sucesso!', 'success');
        loadClientes();
    } catch (error) {
        showToast(error.message || 'Erro ao excluir cliente', 'error');
    }
}

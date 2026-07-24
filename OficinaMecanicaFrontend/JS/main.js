// ==========================================
// Main - Navegação e Lógica da UI
// ==========================================

// ==========================================
// Navegação entre páginas
// ==========================================

document.addEventListener('DOMContentLoaded', () => {
    initNavigation();
    initSidebarToggle();
    setCurrentDate();
    loadDashboard();
});

function initNavigation() {
    const navLinks = document.querySelectorAll('.nav-link');
    navLinks.forEach(link => {
        link.addEventListener('click', (e) => {
            e.preventDefault();
            const page = link.dataset.page;
            navigateTo(page);
        });
    });
}

function navigateTo(page) {
    // Update nav links
    document.querySelectorAll('.nav-link').forEach(link => {
        link.classList.remove('active');
        if (link.dataset.page === page) {
            link.classList.add('active');
        }
    });

    // Show/hide pages
    document.querySelectorAll('.page-content').forEach(p => {
        p.classList.add('hidden');
    });
    const targetPage = document.getElementById(`page-${page}`);
    if (targetPage) {
        targetPage.classList.remove('hidden');
    }

    // Load data for the page
    loadPageData(page);
}

function loadPageData(page) {
    switch (page) {
        case 'dashboard':
            loadDashboard();
            break;
        case 'clientes':
            loadClientes();
            break;
        case 'veiculos':
            loadVeiculos();
            break;
        case 'mecanicos':
            loadMecanicos();
            break;
        case 'equipes':
            loadEquipes();
            break;
        case 'servicos':
            loadServicos();
            break;
        case 'pecas':
            loadPecas();
            break;
        case 'ordens-servico':
            loadOrdensServico();
            break;
    }
}

// ==========================================
// Sidebar Toggle
// ==========================================

function initSidebarToggle() {
    const menuToggle = document.getElementById('menu-toggle');
    const sidebar = document.getElementById('sidebar');
    const mainContent = document.getElementById('main-content');

    menuToggle.addEventListener('click', () => {
        sidebar.classList.toggle('collapsed');
        mainContent.classList.toggle('sidebar-collapsed');
    });
}

// ==========================================
// Data Atual
// ==========================================

function setCurrentDate() {
    const dateEl = document.getElementById('current-date');
    const now = new Date();
    const options = { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' };
    dateEl.textContent = now.toLocaleDateString('pt-BR', options);
}

// ==========================================
// Dashboard
// ==========================================

async function loadDashboard() {
    try {
        const [clientes, veiculos, ordens, mecanicos] = await Promise.all([
            api.get(ENDPOINTS.clientes),
            api.get(ENDPOINTS.veiculos),
            api.get(ENDPOINTS.ordensServico),
            api.get(ENDPOINTS.mecanicos)
        ]);

        document.getElementById('count-clientes').textContent = clientes.length;
        document.getElementById('count-veiculos').textContent = veiculos.length;
        document.getElementById('count-mecanicos').textContent = mecanicos.length;

        const osAbertas = ordens.filter(os => os.status !== 'CONCLUIDA' && os.status !== 'CANCELADA');
        document.getElementById('count-os-abertas').textContent = osAbertas.length;

        const recentOs = ordens.slice(0, 5);
        const tbody = document.getElementById('dashboard-os-table');

        if (recentOs.length === 0) {
            tbody.innerHTML = '<tr><td colspan="5" class="py-4 text-center text-gray-400">Nenhuma ordem encontrada</td></tr>';
            return;
        }

        tbody.innerHTML = recentOs.map(os => `
            <tr>
                <td class="py-2 px-4 text-sm font-medium">#${os.id}</td>
                <td class="py-2 px-4 text-sm">${os.veiculoPlaca || '-'}</td>
                <td class="py-2 px-4 text-sm">${os.equipeNome || '-'}</td>
                <td class="py-2 px-4 text-sm">
                    <span class="status-badge ${getStatusClass(os.status)}">${getStatusLabel(os.status)}</span>
                </td>
                <td class="py-2 px-4 text-sm font-semibold">${formatCurrency(os.valorTotal || 0)}</td>
            </tr>
        `).join('');
    } catch (error) {
        console.error('Erro ao carregar dashboard:', error);
    }
}

// ==========================================
// Modal Functions
// ==========================================

function openModal(modalId) {
    const modal = document.getElementById(modalId);
    modal.classList.remove('hidden');
    document.body.style.overflow = 'hidden';
}

function closeModal(modalId) {
    const modal = document.getElementById(modalId);
    modal.classList.add('hidden');
    document.body.style.overflow = '';
    resetForm(modalId);
    resetModalTitle(modalId);
}

function resetForm(modalId) {
    const form = document.querySelector(`#${modalId} form`);
    if (form) {
        form.reset();
        const idInput = form.querySelector('input[type="hidden"]');
        if (idInput) idInput.value = '';
    }
}

function resetModalTitle(modalId) {
    const titles = {
        'cliente-modal': 'Novo Cliente',
        'veiculo-modal': 'Novo Veículo',
        'mecanico-modal': 'Novo Mecânico',
        'equipe-modal': 'Nova Equipe',
        'servico-modal': 'Novo Serviço',
        'peca-modal': 'Nova Peça'
    };
    const titleEl = document.getElementById(`${modalId}-title`);
    if (titleEl && titles[modalId]) {
        titleEl.textContent = titles[modalId];
    }
}

// ==========================================
// Toast Notifications
// ==========================================

function showToast(message, type = 'info') {
    const container = document.getElementById('toast-container');
    const toast = document.createElement('div');
    toast.className = `toast toast-${type}`;

    const icons = {
        success: 'fa-check-circle',
        error: 'fa-exclamation-circle',
        info: 'fa-info-circle'
    };

    toast.innerHTML = `<i class="fas ${icons[type]}"></i> ${message}`;
    container.appendChild(toast);

    setTimeout(() => {
        toast.remove();
    }, 3000);
}

// ==========================================
// Funções auxiliares para selects
// ==========================================

async function populateSelect(selectId, endpoint, labelField, idField = 'id') {
    try {
        const data = await api.get(endpoint);
        const select = document.getElementById(selectId);
        select.innerHTML = '<option value="">Selecione...</option>';
        data.forEach(item => {
            const option = document.createElement('option');
            option.value = item[idField];
            option.textContent = item[labelField];
            select.appendChild(option);
        });
        return data;
    } catch (error) {
        console.error(`Erro ao carregar dados para ${selectId}:`, error);
        return [];
    }
}

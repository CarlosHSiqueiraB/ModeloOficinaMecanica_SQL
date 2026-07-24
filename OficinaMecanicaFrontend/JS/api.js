// ==========================================
// Configuração da API - Oficina do Carlão
// ==========================================

const API_BASE_URL = window.location.hostname === 'localhost' || window.location.hostname === '127.0.0.1'
    ? 'http://localhost:8080/api'
    : 'https://oficina-mecanica.azurewebsites.net/api';

// ==========================================
// Funções genéricas de requisição HTTP
// ==========================================

const api = {
    async get(endpoint) {
        const response = await fetch(`${API_BASE_URL}${endpoint}`);
        if (!response.ok) {
            const error = await response.json().catch(() => ({}));
            throw new Error(error.erro || `Erro ao buscar dados: ${response.status}`);
        }
        return response.json();
    },

    async post(endpoint, data) {
        const response = await fetch(`${API_BASE_URL}${endpoint}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });
        if (!response.ok) {
            const error = await response.json().catch(() => ({}));
            throw new Error(error.erro || error.message || `Erro ao cadastrar: ${response.status}`);
        }
        return response.json();
    },

    async put(endpoint, data) {
        const response = await fetch(`${API_BASE_URL}${endpoint}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });
        if (!response.ok) {
            const error = await response.json().catch(() => ({}));
            throw new Error(error.erro || error.message || `Erro ao atualizar: ${response.status}`);
        }
        return response.json();
    },

    async patch(endpoint, data) {
        const response = await fetch(`${API_BASE_URL}${endpoint}`, {
            method: 'PATCH',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });
        if (!response.ok) {
            const error = await response.json().catch(() => ({}));
            throw new Error(error.erro || error.message || `Erro ao alterar: ${response.status}`);
        }
        return response.json();
    },

    async delete(endpoint) {
        const response = await fetch(`${API_BASE_URL}${endpoint}`, {
            method: 'DELETE'
        });
        if (!response.ok && response.status !== 204) {
            const error = await response.json().catch(() => ({}));
            throw new Error(error.erro || `Erro ao excluir: ${response.status}`);
        }
        return response.status === 204 ? null : response.json();
    }
};

// ==========================================
// Endpoints da API
// ==========================================

const ENDPOINTS = {
    clientes: '/clientes',
    veiculos: '/veiculos',
    mecanicos: '/mecanicos',
    equipes: '/equipes',
    servicos: '/servicos',
    pecas: '/pecas',
    ordensServico: '/ordens-servico'
};

// ==========================================
// Funções utilitárias
// ==========================================

function formatCurrency(value) {
    return new Intl.NumberFormat('pt-BR', {
        style: 'currency',
        currency: 'BRL'
    }).format(value);
}

function formatDate(dateStr) {
    if (!dateStr) return '-';
    const [year, month, day] = dateStr.split('-');
    return `${day}/${month}/${year}`;
}

function getStatusClass(status) {
    const classes = {
        'EM_ANALISE': 'status-em-analise',
        'EM_ANDAMENTO': 'status-em-andamento',
        'CONCLUIDA': 'status-concluida',
        'CANCELADA': 'status-cancelada'
    };
    return classes[status] || '';
}

function getStatusLabel(status) {
    const labels = {
        'EM_ANALISE': 'Em Análise',
        'EM_ANDAMENTO': 'Em Andamento',
        'CONCLUIDA': 'Concluída',
        'CANCELADA': 'Cancelada'
    };
    return labels[status] || status;
}

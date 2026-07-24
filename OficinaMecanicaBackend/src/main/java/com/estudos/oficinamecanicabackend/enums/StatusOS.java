package com.estudos.oficinamecanicabackend.enums;

// ===========================================
// Enum que representa os possíveis status de uma Ordem de Serviço
// ===========================================
public enum StatusOS {

    EM_ANALISE("Em Análise"),
    EM_ANDAMENTO("Em Andamento"),
    CONCLUIDA("Concluída"),
    CANCELADA("Cancelada");

    private final String descricao;

    StatusOS(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}

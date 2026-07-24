package com.estudos.oficinamecanicabackend.exception;

// ===========================================
// Exceção lançada quando uma entidade não é encontrada no banco
// ===========================================
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String mensagem) {
        super(mensagem);
    }
}

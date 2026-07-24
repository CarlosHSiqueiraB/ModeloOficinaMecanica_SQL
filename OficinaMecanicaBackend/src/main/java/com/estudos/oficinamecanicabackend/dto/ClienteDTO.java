package com.estudos.oficinamecanicabackend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

// ===========================================
// DTO de entrada para Cliente (usado no POST e PUT)
// ===========================================
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClienteDTO {

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @NotBlank(message = "CPF é obrigatório")
    private String cpf;

    private String telefone;

    private String email;

    private String endereco;
}

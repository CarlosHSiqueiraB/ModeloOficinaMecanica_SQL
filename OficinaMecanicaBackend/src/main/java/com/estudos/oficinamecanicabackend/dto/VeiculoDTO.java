package com.estudos.oficinamecanicabackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

// ===========================================
// DTO de entrada para Veiculo (usado no POST e PUT)
// ===========================================
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VeiculoDTO {

    @NotBlank(message = "Marca é obrigatória")
    private String marca;

    @NotBlank(message = "Modelo é obrigatório")
    private String modelo;

    @NotNull(message = "Ano é obrigatório")
    private Integer ano;

    @NotBlank(message = "Placa é obrigatória")
    private String placa;

    private String cor;

    @NotNull(message = "ID do cliente é obrigatório")
    private Long clienteId;
}

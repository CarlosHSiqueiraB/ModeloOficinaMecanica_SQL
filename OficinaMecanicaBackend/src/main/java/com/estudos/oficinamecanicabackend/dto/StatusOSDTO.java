package com.estudos.oficinamecanicabackend.dto;

import com.estudos.oficinamecanicabackend.enums.StatusOS;
import lombok.*;

// ===========================================
// DTO para alterar o status de uma Ordem de Serviço
// ===========================================
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatusOSDTO {

    private StatusOS status;
}

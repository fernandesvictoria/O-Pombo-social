package com.vilu.pombo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DenunciaDTO {
    private String idPruu;
    private Integer quantidadeDenuncias;
    private Integer qntDenunciasPendentes;
    private Integer qntDenunciasAnalisadas;
}

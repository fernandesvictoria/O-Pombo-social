package com.vilu.pombo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PruuDTO {
    private String uuid;
    private String texto;
    private String usuarioId;
    private String nomeUsuario;
    private Integer quantidadeLikes;
    private Integer quantidadeDenuncias;
}
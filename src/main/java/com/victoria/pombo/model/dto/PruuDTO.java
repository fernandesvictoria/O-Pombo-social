package com.victoria.pombo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PruuDTO {
    private String id;
    private String texto;
    private String usuarioId;
    private String nomeUsuario;
    private Integer quantidadeLikes;
    private Integer quantidadeDenuncias;
}

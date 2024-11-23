package com.vilu.pombo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UsuarioDTO {
    private String nome;
    private String email;
    private String cpf;
    private String senha;
}
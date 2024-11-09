package com.victoria.pombo.factories;

import com.victoria.pombo.model.entity.Usuario;
import com.victoria.pombo.model.enums.Role;

public class UsuarioFactory {
    public static Usuario createUsuario() {
        Usuario usuario = new Usuario();
        usuario.setNome("Lewis Hamilton");
        usuario.setEmail("lewis@hamilton.com");
        usuario.setCpf("12345678901");
        usuario.setRole(Role.USER);
        usuario.setSenha("mercedes");
        return usuario;
    }
}

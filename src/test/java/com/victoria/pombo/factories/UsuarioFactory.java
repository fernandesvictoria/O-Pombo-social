package com.victoria.pombo.factories;

import com.victoria.pombo.model.entity.Usuario;

public class UsuarioFactory {
    public static Usuario createUsuario() {
        Usuario usuario = new Usuario();
        usuario.setNome("Lewis Hamilton");
        usuario.setEmail("lewis@hamilton.com");
        usuario.setCpf("12345678901");
        usuario.setIsAdmin(false);
        return usuario;
    }
}

package com.vilu.pombo.factory;

import com.vilu.pombo.model.entity.Usuario;
import com.vilu.pombo.model.enums.Perfil;

import java.util.UUID;

public class UsuarioFactory {

    public static Usuario criarUsuarioAdmin() {
        Usuario usuario = new Usuario();
        usuario.setUuid(UUID.randomUUID().toString());
        usuario.setNome("Lewis Hamilton");
        usuario.setEmail("lewis@hamilton.com");
        usuario.setCpf("44444444444");
        usuario.setSenha("mercedes");
        usuario.setPerfil(Perfil.ADMINISTRADOR);
        usuario.setAdmin(true);
        return usuario;
    }

    public static Usuario criarUsuarioMaxVerstappen() {
        Usuario usuario = new Usuario();
        usuario.setUuid(UUID.randomUUID().toString());
        usuario.setNome("Max Verstappen");
        usuario.setEmail("max@verstappen.com");
        usuario.setCpf("33333333333");
        usuario.setSenha("redbullracing");
        usuario.setPerfil(Perfil.USUARIO);
        usuario.setAdmin(false);
        return usuario;
    }
}

package com.vilu.pombo.factory;

import com.vilu.pombo.model.entity.Pruu;
import com.vilu.pombo.model.entity.Usuario;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;

public class PruuFactory {

    public static Pruu criarPruuPadrao() {
        Pruu pruu = new Pruu();
        pruu.setUuid(UUID.randomUUID().toString());
        pruu.setTexto("Este é um pruu de exemplo.");
        pruu.setDataHoraCriacao(LocalDateTime.now());
        pruu.setQuantidadeLikes(0);
        pruu.setUsuario(null); // nenhum usuário associado por padrão
        pruu.setUsuariosQueCurtiram(Collections.emptyList());
        pruu.setImagemEmBase64(null); // sem imagem por padrão
        pruu.setBloqueado(false);
        return pruu;
    }

    public static Pruu criarPruuComUsuario(Usuario usuario) {
        Pruu pruu = criarPruuPadrao();
        pruu.setUsuario(usuario);
        return pruu;
    }

    public static Pruu criarPruuComTextoEUsuario(String texto, Usuario usuario) {
        Pruu pruu = criarPruuPadrao();
        pruu.setTexto(texto);
        pruu.setUsuario(usuario);
        return pruu;
    }

    public static Pruu criarPruuComImagem(String texto, String imagemBase64, Usuario usuario) {
        Pruu pruu = criarPruuPadrao();
        pruu.setTexto(texto);
        pruu.setImagemEmBase64(imagemBase64);
        pruu.setUsuario(usuario);
        return pruu;
    }
}


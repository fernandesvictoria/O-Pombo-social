package com.victoria.pombo.factories;

import com.victoria.pombo.model.entity.Pruu;
import com.victoria.pombo.model.entity.Usuario;

public class PruuFactory {
    public static Pruu createPruu(Usuario usuario) {
        Pruu pruu = new Pruu();
        pruu.setTexto("Estou de mudança para a Ferrari em 2025! #ForzaFerrari");
        pruu.setUsuario(usuario);
        pruu.setBloqueado(false);
        return pruu;
    }
}
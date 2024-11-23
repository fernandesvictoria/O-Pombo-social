package com.vilu.pombo.factory;

import com.vilu.pombo.model.entity.Denuncia;
import com.vilu.pombo.model.enums.Motivo;

public class DenunciaFactory {

    public static Denuncia criarDenuncia(String pruuId, String usuarioId, Motivo motivo) {
        Denuncia denuncia = new Denuncia();
        denuncia.setPruuId(pruuId);
        denuncia.setUsuarioId(usuarioId);
        denuncia.setMotivo(motivo);
        return denuncia;
    }

    public static Denuncia criarDenunciaComMotivoSpam(String pruuId, String usuarioId) {
        return criarDenuncia(pruuId, usuarioId, Motivo.SPAM);
    }

    public static Denuncia criarDenunciaComMotivoDiscursoOdio(String pruuId, String usuarioId) {
        return criarDenuncia(pruuId, usuarioId, Motivo.DISCURSO_ODIO);
    }

    public static Denuncia criarDenunciaComMotivoConteudoInapropriado(String pruuId, String usuarioId) {
        return criarDenuncia(pruuId, usuarioId, Motivo.CONTEUDO_INAPROPRIADO);
    }

    public static Denuncia criarDenunciaDefault() {
        return criarDenunciaComMotivoSpam("uuidPruuTest", "uuidUsuarioTest");
    }
}


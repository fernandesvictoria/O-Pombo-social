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

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String uuid;
        private String texto;
        private String usuarioId;
        private String nomeUsuario;
        private Integer quantidadeLikes;
        private Integer quantidadeDenuncias;

        public Builder uuid(String uuid) {
            this.uuid = uuid;
            return this;
        }

        public Builder texto(String texto) {
            this.texto = texto;
            return this;
        }

        public Builder usuarioId(String usuarioId) {
            this.usuarioId = usuarioId;
            return this;
        }

        public Builder nomeUsuario(String nomeUsuario) {
            this.nomeUsuario = nomeUsuario;
            return this;
        }

        public Builder quantidadeLikes(Integer quantidadeLikes) {
            this.quantidadeLikes = quantidadeLikes;
            return this;
        }

        public Builder quantidadeDenuncias(Integer quantidadeDenuncias) {
            this.quantidadeDenuncias = quantidadeDenuncias;
            return this;
        }

        public PruuDTO build() {
            return new PruuDTO(uuid, texto, usuarioId, nomeUsuario, quantidadeLikes, quantidadeDenuncias);
        }
    }
}

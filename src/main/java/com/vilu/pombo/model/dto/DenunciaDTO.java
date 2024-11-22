package com.vilu.pombo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DenunciaDTO {
    private String idPruu;
    private Integer quantidadeDenuncias;

    public static DenunciaDTOBuilder builder() {
        return new DenunciaDTOBuilder();
    }

    public static class DenunciaDTOBuilder {
        private String idPruu;
        private Integer quantidadeDenuncias;

        public DenunciaDTOBuilder idPruu(String idPruu) {
            this.idPruu = idPruu;
            return this;
        }

        public DenunciaDTOBuilder quantidadeDenuncias(Integer quantidadeDenuncias) {
            this.quantidadeDenuncias = quantidadeDenuncias;
            return this;
        }

        public DenunciaDTO build() {
            return new DenunciaDTO(idPruu, quantidadeDenuncias);
        }
    }
}

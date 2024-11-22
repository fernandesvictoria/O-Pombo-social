package com.vilu.pombo.model.entity;

import com.vilu.pombo.model.dto.DenunciaDTO;
import com.vilu.pombo.model.enums.Motivo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;

@Table
@Entity
@Data
public class Denuncia {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @UuidGenerator
    private String uuid;

    @Column(nullable = false)
    private LocalDateTime dataHoraDenuncia;

    @Column(name = "pruu_id", nullable = false)
    private String pruuId;

    @Column(name = "usuario_id", nullable = false)
    private String usuarioId;

    @Schema(description = "Motivo da Denúncia", example = "SPAM", allowableValues = {"SPAM", "DISCURSO_ODIO",
            "CONTEUDO_INAPROPRIADO"})
    @Column(nullable = false)
    private Motivo motivo;

    @PrePersist
    protected void onCreate() {
        dataHoraDenuncia = LocalDateTime.now();
    }

    public static DenunciaDTO toDTO(String idPruu, Integer quantidadeDenuncias, Integer qntDenunciasPendentes,
                                    Integer qntDenunciasAnalisadas) {
        return new DenunciaDTO(idPruu, quantidadeDenuncias, qntDenunciasPendentes, qntDenunciasAnalisadas);
    }
}
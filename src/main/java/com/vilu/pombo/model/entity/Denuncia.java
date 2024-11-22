package com.vilu.pombo.model.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.UuidGenerator;

import com.vilu.pombo.model.enums.Motivo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;

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

    @Schema(description = "Motivo da Denúncia", example = "SPAM", allowableValues = {"SPAM", "DISCURSO_ODIO", "CONTEUDO_INAPROPRIADO"})
    @Column(nullable = false)
    private Motivo motivo;

    @PrePersist
    protected void onCreate() {
        dataHoraDenuncia = LocalDateTime.now();
    }

	public String getPruuId() {
		return pruuId;
	}

	public String getUsuarioId() {
		return usuarioId;
	}
    
}
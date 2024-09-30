package com.victoria.pombo.model.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import com.victoria.pombo.model.dto.DenunciaDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;


@Entity
@Data
@Table(name = "denuncias")
public class Denuncia {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@UuidGenerator
	private String id;

	@Column(updatable = false)
	@CreationTimestamp
	private LocalDateTime dataCriacao;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pruu_id", nullable = false)
	private Pruu pruu;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "usuario_id", nullable = false)
	private Usuario user;

	@Schema(description = "Motivo Denuncia", example = "SPAM", allowableValues = { "SPAM", "DISCURSO_ODIO",
			"CONTEUDO_INAPROPRIADO" })
	@Column(nullable = false)
	private Motivo motivo;

	@ColumnDefault("false")
	private Boolean analisada = false;

	public static DenunciaDTO toDTO(String idPruu, Integer quantidadeDenuncias, Integer qntDenunciasPendentes,
			Integer qntDenunciasAnalisadas) {
		return new DenunciaDTO(idPruu, quantidadeDenuncias, qntDenunciasPendentes, qntDenunciasAnalisadas);
	}
}

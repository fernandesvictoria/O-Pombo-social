package com.victoria.pombo.model.entity;

import java.time.LocalDateTime;
import java.util.List;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.victoria.pombo.model.dto.PruuDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table
@NoArgsConstructor
public class Pruu {

	@Id
	@UuidGenerator
	private String uuid;

	@NotBlank(message = "Pruu text is required")
	@Size(min = 1, max = 300, message = "Número de caracteres inválido")
	private String texto;

	@Column(updatable = false)
	@CreationTimestamp
	private LocalDateTime dataCriacao;

	@Column
	private Integer quantidadeLikes = 0;

	@ManyToOne
	@JoinColumn(name = "user_id")
	@JsonBackReference
	private Usuario usuario;

	
	@ManyToMany
	@JoinTable(name = "usuarios_curtiram_pruus", joinColumns = @JoinColumn(name = "pruu_id"), inverseJoinColumns = @JoinColumn(name = "usuario_id"))
	private List<Usuario> likes;

	@Column
	private boolean bloqueado;
	
	 @OneToMany(mappedBy = "denuncias")
	    @JsonBackReference
	    private List<Denuncia> denuncias;

	public static PruuDTO toDTO(Pruu pruu, Integer qtdLikes, Integer qtdDenuncias) {
		if (!pruu.isBloqueado()) {
			pruu.setTexto("Bloqueado");
		}

		return new PruuDTO(pruu.getUuid(), pruu.getTexto(), pruu.getUsuario().getId().toString(),
				pruu.getUsuario().getNome(), qtdLikes, qtdDenuncias);
	}
}

package com.victoria.pombo.model.entity;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;
import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Pruu {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID uuid;

	@NotBlank(message = "Pruu text is required")
	@Size(min = 1, max = 300, message = "Número de caracteres inválido")
	private String texto;

	@Column(nullable = false, updatable = false)
	@CreationTimestamp
	private LocalDateTime dataCriacao;

	@Column(nullable = false)
	private Integer quantidadeLikes = 0;
	// Relacionamento muitos-para-um: cada Pruu é criado por um único usuário
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	@JsonBackReference
	private Usuario usuario;

	// Relacionamento muitos-para-muitos: vários usuários podem curtir um Pruu
	@ManyToMany(mappedBy = "pruusCurtidos")
	private List<Usuario> likes;

	@Column(nullable = false)
	private boolean bloqueado;
}

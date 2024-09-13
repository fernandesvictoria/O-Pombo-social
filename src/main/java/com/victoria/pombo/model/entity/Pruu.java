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
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Data
@Table
public class Pruu {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID uuid;

	@NotBlank(message = "Pruu text is required")
	@Size(min = 1, max = 300, message = "Número de caracteres inválido")
	private String texto;

	@Column(updatable = false)
	@CreationTimestamp 
	private LocalDateTime dataCriacao;

	@Column
	private Integer quantidadeLikes = 0;

	// Relacionamento muitos-para-um: cada Pruu é criado por um único usuário
	@ManyToOne
	@JoinColumn(name = "user_id")
	@JsonBackReference
	private Usuario usuario;

	// Relacionamento muitos-para-muitos: vários usuários podem curtir um Pruu
	@ManyToMany
	@JoinTable(name = "usuarios_curtiram_pruus",
			   joinColumns = @JoinColumn(name = "pruu_id"),
	           inverseJoinColumns = @JoinColumn(name = "usuario_id"))
	private List<Usuario> likes;

	@Column
	private boolean bloqueado;
}

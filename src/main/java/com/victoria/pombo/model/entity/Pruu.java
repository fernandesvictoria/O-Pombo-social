package com.victoria.pombo.model.entity;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;
import org.hibernate.annotations.CreationTimestamp;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
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
	private int quantidadeLikes;
	
	
	// Relacionamento muitos-para-um com a entidade Usuario
	// Cada Pruu está associado a um único usuário
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private Usuario usuario;

	// Relacionamento muitos-para-muitos com a entidade Usuario para representar
	// quem curtiu o Pruu
	@ManyToMany
	@JoinTable(name = "pruu_likes", // Nome da tabela de junção
			joinColumns = @JoinColumn(name = "pruu_id"), // Coluna que referencia a entidade Pruu
			inverseJoinColumns = @JoinColumn(name = "user_id") // Coluna que referencia a entidade Usuario
	)
	private List<Usuario> likes; // Lista de usuários que curtiram o Pruu


	@Column(nullable = false)
	private boolean bloqueado;
}

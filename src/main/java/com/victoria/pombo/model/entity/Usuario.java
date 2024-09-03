package com.victoria.pombo.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Data
@Table
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@NotBlank
	@Column(nullable = false, unique = true)
	private String nome;

	@NotBlank
	@Email(message = "Email should be valid")
	@Column(nullable = false, unique = true)
	private String email;

	@NotBlank(message = "CPF é necessário")
	@Pattern(regexp = "\\d{11}")
	@Column(unique = true)
	private String cpf;

	@Column(nullable = false)
	private boolean isAdmin;

	// Relacionamento de um-para-muitos com a entidade Pruu
    // O usuário pode ter vários pruus associados a ele
	@OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	 @JsonManagedReference
	private List<Pruu> pruus; // Lista de pruus enviados pelo usuário

}

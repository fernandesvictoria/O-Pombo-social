package com.victoria.pombo.model.entity;

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
import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.validator.constraints.Length;

@Entity
@Data
@Table
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@NotBlank(message = "Nome é necessário")
	@Column(unique = true)
	@Length(min = 3, max = 200, message = "Nome deve ter entre 3 e 200 caracteres")
	private String nome;

	@NotBlank
	@Email(message = "Email deve ser valido")
	@Column(unique = true)
	private String email;

	@NotBlank(message = "CPF é necessário")
	@Pattern(regexp = "\\d{11}", message = "CPF deve conter exatamente 11 dígitos")
	@Column(unique = true, nullable = false)
	private String cpf;

	@Column(nullable = false)
	private boolean isAdmin;

	// Relacionamento de um-para-muitos: um usuário pode criar vários pruus
	@OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY)
	@JsonBackReference
	private List<Pruu> pruus;

}

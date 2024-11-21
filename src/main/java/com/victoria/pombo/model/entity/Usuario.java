package com.victoria.pombo.model.entity;

import com.victoria.pombo.model.dto.UsuarioDTO;
import com.victoria.pombo.model.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Data
@Table
public class Usuario implements UserDetails {

	private static final long serialVersionUID = 3667682428012659277L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@NotBlank(message = "Nome é necessário")
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

	@Enumerated(EnumType.STRING)
	private Role role = Role.USER;

	@NotBlank(message = "A senha não deve estar em branco.")
	@Size(max = 500)
	private String senha;

	// Relacionamento de um-para-muitos: um usuário pode criar vários pruus
	@OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY)
	@JsonBackReference
	private List<Pruu> pruus;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> list = new ArrayList<GrantedAuthority>();
		list.add(new SimpleGrantedAuthority(role.toString()));
		return list;
	}

	@Override
	public String getPassword() {
		return this.senha;
	}

	@Override
	public String getUsername() {
		return this.email;
	}

	public static Usuario fromDTO(UsuarioDTO dto) {
		Usuario u = new Usuario();
		u.setCpf(dto.getCpf());
		u.setEmail(dto.getEmail());
		u.setNome(dto.getNome());
		u.setSenha(dto.getSenha());
		
		return u;
	}
}

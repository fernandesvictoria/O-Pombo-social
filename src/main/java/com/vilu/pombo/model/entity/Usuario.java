package com.vilu.pombo.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vilu.pombo.model.dto.UsuarioDTO;
import com.vilu.pombo.model.enums.Perfil;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table
@Data
public class Usuario implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @UuidGenerator
    @Column(nullable = false, unique = true)
    private String uuid;

    @NotBlank(message = "É obrigatório informar o nome.")
    @Size(min = 3, max = 200, message = "O nome deve ter entre 3 e 200 caracteres.")
    private String nome;

    @NotBlank
    @Email(message = "O email deve ser válido.")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "É obrigatório informar o CPF.")
    @Pattern(regexp = "\\d{11}", message = "CPF deve conter exatamente 11 dígitos.")
    @Column(unique = true, nullable = false)
    private String cpf;

    @NotBlank(message = "A senha não deve estar em branco.")
    @Size(max = 500)
    private String senha;

    @Enumerated(EnumType.STRING)
    private Perfil perfil = Perfil.USUARIO;

    @NotNull(message = "É obrigatório informar se o usuário é administrador.")
    private boolean isAdmin;

    @JsonBackReference
    @OneToMany(mappedBy = "usuario")
    private List<Pruu> pruss = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "Usuario_like", joinColumns = @JoinColumn(name = "id_usuario"), inverseJoinColumns = @JoinColumn(name = "id_pruu"))
    @JsonIgnore
    private List<Pruu> pruusCurtidos;

    @Override
    public java.util.Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> list = new ArrayList<GrantedAuthority>();

        list.add(new SimpleGrantedAuthority(perfil.toString()));

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

}

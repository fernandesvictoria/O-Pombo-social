package com.victoria.pombo.model.repository;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.victoria.pombo.factories.UsuarioFactory;
import com.victoria.pombo.model.entity.Usuario;
import com.victoria.pombo.model.enums.Role;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.TransactionSystemException;

@SpringBootTest
@ActiveProfiles("test")
public class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    @DisplayName("Deve lançar uma exceção ao tentar inserir um usuário com nome inválido (mais de 200 caracteres)")
    public void testInsert$nomeMaisDe200Caracteres() {
        String nome = "a".repeat(201); // cria uma string com 201 caracteres
        Usuario usuario = new Usuario();
        usuario.setNome(nome);
        usuario.setEmail("lewis@hamilton.com");
        usuario.setCpf("12345678901");
        usuario.setRole(Role.USER);

        assertThatThrownBy(() -> usuarioRepository.save(usuario))
            .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    @DisplayName("Deve lançar uma exceção ao tentar inserir um usuário com email inválido")
    public void testInsert$emailInvalido() {
        Usuario usuario = UsuarioFactory.createUsuario();
        usuario.setEmail("lewis@hamilton"); // email inválido

        assertThatThrownBy(() -> usuarioRepository.save(usuario))
            .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    @DisplayName("Deve lançar uma exceção ao tentar inserir um usuário com CPF inválido (menos de 11 dígitos)")
    public void testInsert$cpfInvalido() {
        Usuario usuario = UsuarioFactory.createUsuario();
        usuario.setCpf("12345"); // CPF inválido

        assertThatThrownBy(() -> usuarioRepository.save(usuario))
            .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    @DisplayName("Deve lançar uma exceção ao tentar inserir um usuário sem senha")
    public void testInsert$userWithoutPassword() {
        Usuario usuario = UsuarioFactory.createUsuario();
        usuario.setSenha(null);
        assertThatThrownBy(() -> usuarioRepository.saveAndFlush(usuario)).isInstanceOf(ConstraintViolationException.class)
                .hasMessageContaining("A senha não pode estar em branco.");
    }
}

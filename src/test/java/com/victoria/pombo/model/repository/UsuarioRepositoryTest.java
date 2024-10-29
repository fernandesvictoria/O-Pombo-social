package com.victoria.pombo.model.repository;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.victoria.pombo.model.entity.Usuario;
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
        usuario.setAdmin(false);

        assertThatThrownBy(() -> usuarioRepository.save(usuario))
            .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    @DisplayName("Deve lançar uma exceção ao tentar inserir um usuário com email inválido")
    public void testInsert$emailInvalido() {
        Usuario usuario = new Usuario();
        usuario.setNome("Fernando Alonso");
        usuario.setEmail("emailinvalido");
        usuario.setCpf("12345678901");
        usuario.setAdmin(false);

        assertThatThrownBy(() -> usuarioRepository.save(usuario))
            .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    @DisplayName("Deve lançar uma exceção ao tentar inserir um usuário com CPF inválido (menos de 11 dígitos)")
    public void testInsert$cpfInvalido() {
        Usuario usuario = new Usuario();
        usuario.setNome("Max Verstappen");
        usuario.setEmail("max@verstappen.com");
        usuario.setCpf("1234567"); // CPF com menos de 11 dígitos
        usuario.setAdmin(false);

        assertThatThrownBy(() -> usuarioRepository.save(usuario))
            .isInstanceOf(ConstraintViolationException.class);
    }
}

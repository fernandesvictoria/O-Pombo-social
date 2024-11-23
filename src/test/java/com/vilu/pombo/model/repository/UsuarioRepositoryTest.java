package com.vilu.pombo.model.repository;

import com.vilu.pombo.factory.UsuarioFactory;
import com.vilu.pombo.model.entity.Usuario;
import com.vilu.pombo.model.enums.Perfil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    void deveSalvarUsuarioComSucesso() {
        Usuario usuario = UsuarioFactory.criarUsuarioMaxVerstappen();

        Usuario usuarioSalvo = usuarioRepository.save(usuario);

        assertNotNull(usuarioSalvo.getUuid());
        assertEquals("Max Verstappen", usuarioSalvo.getNome());
        assertEquals("max@verstappen.com", usuarioSalvo.getEmail());
        assertEquals("33333333333", usuarioSalvo.getCpf());
        assertEquals(Perfil.USUARIO, usuarioSalvo.getPerfil());
        assertFalse(usuarioSalvo.isAdmin());
    }

    @Test
    void deveEncontrarUsuarioPorEmail() {
        Usuario usuario = UsuarioFactory.criarUsuarioMaxVerstappen();
        usuarioRepository.save(usuario);

        Optional<Usuario> usuarioEncontrado = usuarioRepository.findByEmail("max@verstappen.com");

        assertTrue(usuarioEncontrado.isPresent());
        assertEquals("Max Verstappen", usuarioEncontrado.get().getNome());
    }

    @Test
    void deveRetornarVazioAoProcurarUsuarioPorEmailInexistente() {
        Optional<Usuario> usuarioEncontrado = usuarioRepository.findByEmail("nao.existe@exemplo.com");

        assertFalse(usuarioEncontrado.isPresent());
    }

    @Test
    void deveExcluirUsuario() {
        Usuario usuario = UsuarioFactory.criarUsuarioMaxVerstappen();
        Usuario usuarioSalvo = usuarioRepository.save(usuario);

        usuarioRepository.delete(usuarioSalvo);

        Optional<Usuario> usuarioExcluido = usuarioRepository.findById(usuarioSalvo.getUuid());
        assertFalse(usuarioExcluido.isPresent());
    }
}

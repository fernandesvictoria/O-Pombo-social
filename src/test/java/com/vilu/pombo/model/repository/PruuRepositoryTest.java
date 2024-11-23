package com.vilu.pombo.model.repository;

import com.vilu.pombo.factory.PruuFactory;
import com.vilu.pombo.factory.UsuarioFactory;
import com.vilu.pombo.model.entity.Pruu;
import com.vilu.pombo.model.entity.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class PruuRepositoryTest {

    @Autowired
    private PruuRepository pruuRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = usuarioRepository.save(UsuarioFactory.criarUsuarioMaxVerstappen());
    }

    @Test
    void deveSalvarPruuComSucesso() {
        Pruu pruu = PruuFactory.criarPruuPadrao();

        Pruu pruuSalvo = pruuRepository.save(pruu);

        assertNotNull(pruuSalvo.getUuid());
        assertEquals(usuario.getUuid(), pruuSalvo.getUsuario().getUuid());
        assertEquals("Max Verstappen", pruuSalvo.getUsuario().getNome());
        assertEquals("Texto do Pruu", pruuSalvo.getTexto());
        assertFalse(pruuSalvo.isBloqueado());
    }

    @Test
    void deveEncontrarPruuPorUuid() {
        Pruu pruu = PruuFactory.criarPruuPadrao();
        pruuRepository.save(pruu);

        Optional<Pruu> pruuEncontrado = pruuRepository.findById(pruu.getUuid());

        assertTrue(pruuEncontrado.isPresent());
        assertEquals(pruu.getUuid(), pruuEncontrado.get().getUuid());
    }

    @Test
    void deveRetornarVazioAoProcurarPruuPorUuidInexistente() {
        Optional<Pruu> pruuEncontrado = pruuRepository.findById("uuidInexistente");

        assertFalse(pruuEncontrado.isPresent());
    }

    @Test
    void deveExcluirPruu() {
        Pruu pruu = PruuFactory.criarPruuPadrao();
        Pruu pruuSalvo = pruuRepository.save(pruu);

        pruuRepository.delete(pruuSalvo);

        Optional<Pruu> pruuExcluido = pruuRepository.findById(pruuSalvo.getUuid());
        assertFalse(pruuExcluido.isPresent());
    }

    @Test
    void deveAtualizarQuantidadeLikesDoPruu() {
        Pruu pruu = PruuFactory.criarPruuPadrao();
        Pruu pruuSalvo = pruuRepository.save(pruu);

        pruuSalvo.setQuantidadeLikes(10);
        Pruu pruuAtualizado = pruuRepository.save(pruuSalvo);

        assertEquals(10, pruuAtualizado.getQuantidadeLikes());
    }
}

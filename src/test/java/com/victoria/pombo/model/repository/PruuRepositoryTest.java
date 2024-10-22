package com.victoria.pombo.model.repository;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.victoria.pombo.model.entity.Pruu;
import com.victoria.pombo.model.entity.Usuario;
import com.victoria.pombo.factories.PruuFactory;
import com.victoria.pombo.factories.UsuarioFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.TransactionSystemException;

@SpringBootTest
@ActiveProfiles("test")
public class PruuRepositoryTest {

    @Autowired
    private PruuRepository pruuRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @BeforeEach
    public void setUp() {
        pruuRepository.deleteAll();
        usuarioRepository.deleteAll();
    }

    @Test
    @DisplayName("Não se pode inserir um Pruu com mais de 300 caracteres no texto")
    public void testInsert$textoMaisDe300Caracteres() {
        Usuario usuario = UsuarioFactory.createUsuario(); // cria um usuário válido
        usuarioRepository.save(usuario);

        Pruu pruu = PruuFactory.createPruu(usuario); // cria um pruu com o usuário
        String texto = "a".repeat(310); // texto com mais de 300 caracteres
        pruu.setTexto(texto);

        assertThatThrownBy(() -> pruuRepository.save(pruu))
                .isInstanceOf(TransactionSystemException.class);
    }

    @Test
    @DisplayName("Deve lançar uma exceção ao tentar inserir um Pruu com um texto vazio")
    public void testInsert$textoVazio() {
        Usuario usuario = UsuarioFactory.createUsuario(); // cria um usuário válido
        usuarioRepository.save(usuario);

        Pruu pruu = PruuFactory.createPruu(usuario); // cria um pruu com o usuário
        pruu.setTexto(""); // seta o texto como vazio

        assertThatThrownBy(() -> pruuRepository.save(pruu))
                .isInstanceOf(TransactionSystemException.class);
    }
}

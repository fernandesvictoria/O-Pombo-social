package com.victoria.pombo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import com.victoria.pombo.exception.OpomboException;
import com.victoria.pombo.model.dto.PruuDTO;
import com.victoria.pombo.model.entity.Denuncia;
import com.victoria.pombo.model.entity.Pruu;
import com.victoria.pombo.model.entity.Usuario;
import com.victoria.pombo.model.repository.PruuRepository;
import com.victoria.pombo.model.repository.UsuarioRepository;
import com.victoria.pombo.model.seletor.PruuSeletor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@ActiveProfiles("test")
public class PruuServiceTest {

    @Mock
    private PruuRepository pruuRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private PruuService pruuService;

    private Usuario usuario;
    private Pruu pruu;

    @BeforeEach
    public void setUp() {
        usuario = new Usuario();
        usuario.setId(1);
        usuario.setNome("Ayrton Senna");

        pruu = new Pruu();
        pruu.setUuid("pruu-01");
        pruu.setUsuario(usuario);
    }

    @Test
    @DisplayName("Deve inserir um Pruu na base de dados")
    public void testInserir$success() throws OpomboException {
        when(usuarioRepository.findById(usuario.getId())).thenReturn(Optional.of(usuario));
        when(pruuRepository.save(pruu)).thenReturn(pruu);

        pruuService.inserir(pruu);

        verify(pruuRepository, times(1)).save(pruu);
        assertThat(pruu.getUsuario()).isEqualTo(usuario);
    }

    @Test
    @DisplayName("Deve lançar exceção ao inserir Pruu com usuário inexistente")
    public void testInserir$UserNotFound() throws OpomboException {
        when(usuarioRepository.findById(usuario.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> pruuService.inserir(pruu))
                .isInstanceOf(OpomboException.class)
                .hasMessageContaining("Usuário não encontrado");
    }

    @Test
    @DisplayName("Deve listar todos os Pruu")
    public void testPesquisarTodos$success() {
        List<Pruu> pruus = new ArrayList<>();
        pruus.add(pruu);

        when(pruuRepository.findAll()).thenReturn(pruus);

        List<Pruu> result = pruuService.pesquisarTodos();

        assertThat(result).isNotEmpty();
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("Deve listar todos os Pruu por ID")
    public void testPesquisarPorId$success() throws OpomboException {
        when(pruuRepository.findById(pruu.getUuid())).thenReturn(Optional.of(pruu));

        Optional<Pruu> result = pruuService.pesquisarPorId(pruu.getUuid());

        assertThat(result).isPresent();
        assertThat(result.get().getUuid()).isEqualTo(pruu.getUuid());
    }

    @Test
    @DisplayName("Deve lançar uma exceção ao pesquisar um Pruu por ID que não existe")
    public void testPesquisarPorId$NotFound() throws OpomboException {
        when(pruuRepository.findById(pruu.getUuid())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> pruuService.pesquisarPorId(pruu.getUuid()))
                .isInstanceOf(OpomboException.class)
                .hasMessageContaining("Pruu com ID " + pruu.getUuid() + " não encontrado.");
    }

    @Test
    @DisplayName("Deve curtir um Pruu")
    public void testCurtirPruu$success() throws OpomboException {
        when(pruuRepository.findById(pruu.getUuid())).thenReturn(Optional.of(pruu));
        when(usuarioRepository.findById(usuario.getId())).thenReturn(Optional.of(usuario));

        // teste curtindo
        pruuService.curtirPruu(pruu.getUuid(), usuario.getId());
        assertThat(pruu.getLikes()).contains(usuario);
        assertThat(pruu.getQuantidadeLikes()).isEqualTo(1);

        // teste dando deslike
        pruuService.curtirPruu(pruu.getUuid(), usuario.getId());
        assertThat(pruu.getLikes()).doesNotContain(usuario);
        assertThat(pruu.getQuantidadeLikes()).isEqualTo(0);
    }

    @Test
    @DisplayName("Deve lançar uma exceção ao curtir um Pruu com usuário inexistente")
    public void testCurtirPruu$UserNotFound() throws OpomboException {
        when(pruuRepository.findById(pruu.getUuid())).thenReturn(Optional.of(pruu));
        when(usuarioRepository.findById(usuario.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> pruuService.curtirPruu(pruu.getUuid(), usuario.getId()))
                .isInstanceOf(OpomboException.class)
                .hasMessageContaining("Usuário não encontrado");
    }

    @Test
    @DisplayName("Deve gerar o PruuDTO")
    public void testGerarDTO$success() throws OpomboException {
        List<Pruu> pruus = new ArrayList<>();
        pruus.add(pruu);

        when(pruuRepository.findAll()).thenReturn(pruus);
        when(pruuRepository.findById(pruu.getUuid())).thenReturn(Optional.of(pruu));
        when(usuarioRepository.findById(usuario.getId())).thenReturn(Optional.of(usuario));

        List<PruuDTO> result = pruuService.gerarDTO();

        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getId()).isEqualTo(pruu.getUuid());
    }

    @Test
    @DisplayName("Deve bloquear um Pruu")
    public void testBloquearPruu$success() throws OpomboException {
        when(pruuRepository.findById(pruu.getUuid())).thenReturn(Optional.of(pruu));

        pruuService.bloquear(pruu.getUuid());

        assertThat(pruu.isBloqueado()).isTrue();
        verify(pruuRepository, times(1)).save(pruu);
    }
}

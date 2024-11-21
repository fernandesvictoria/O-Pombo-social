package com.victoria.pombo.service;

import com.victoria.pombo.exception.OpomboException;
import com.victoria.pombo.factories.UsuarioFactory;
import com.victoria.pombo.model.entity.Pruu;
import com.victoria.pombo.model.entity.Usuario;
import com.victoria.pombo.model.enums.Role;
import com.victoria.pombo.model.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Deve inserir um usuário na base de dados")
    public void testInserir$success() throws OpomboException {
        Usuario novoUsuario = UsuarioFactory.createUsuario();
        novoUsuario.setNome("Sebastian Vettel");
        novoUsuario.setEmail("seb@rbr.com");

        when(usuarioRepository.save(novoUsuario)).thenReturn(novoUsuario);
        Usuario result = usuarioService.inserir(novoUsuario);

        assertThat(result).isNotNull();
        assertThat(result.getNome()).isEqualTo(novoUsuario.getNome());
        assertThat(result.getEmail()).isEqualTo(novoUsuario.getEmail());
    }

    @Test
    @DisplayName("Deve listar todos os usuários")
    public void testPesquisarTodos$success() {
        List<Usuario> usuarios = new ArrayList<>();
        Usuario usuario = new Usuario();
        usuario.setNome("Nico Rosberg");
        usuarios.add(usuario);

        when(usuarioRepository.findAll()).thenReturn(usuarios);
        List<Usuario> result = usuarioService.pesquisarTodos();

        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getNome()).isEqualTo(usuario.getNome());
    }

    @Test
    @DisplayName("Deve pesquisar um usuário por ID")
    public void testPesquisarPorId$success() throws OpomboException {
        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setNome("Charles Leclerc");

        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));
        Usuario result = usuarioService.pesquisarPorId(1);

        assertThat(result).isNotNull();
        assertThat(result.getNome()).isEqualTo(usuario.getNome());
    }

    @Test
    @DisplayName("Deve lançar exceção ao pesquisar um usuário por ID que não existe")
    public void testPesquisarPorId$notFound() {
        when(usuarioRepository.findById(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usuarioService.pesquisarPorId(1))
                .isInstanceOf(OpomboException.class)
                .hasMessageContaining("Usuário não encontrado.");
    }

    @Test
    @DisplayName("Deve atualizar um usuário")
    public void testAtualizar$success() throws OpomboException {
        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setNome("Felipe Massa");
        usuario.setEmail("felipe@massa.com");

        when(usuarioRepository.save(usuario)).thenReturn(usuario);
        Usuario result = usuarioService.atualizar(usuario);

        assertThat(result).isNotNull();
        assertThat(result.getNome()).isEqualTo("Felipe Massa");
        assertThat(result.getEmail()).isEqualTo("felipe@massa.com");
    }

    @Test
    @DisplayName("Deve excluir um usuário")
    public void testExcluir$success() throws OpomboException {
        Usuario usuario = new Usuario();
        usuario.setId(1);

        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));
        usuarioService.excluir(1);

        verify(usuarioRepository, times(1)).deleteById(1);
    }

    @Test
    @DisplayName("Deve lançar exceção ao excluir um usuário que contém Pruu")
    public void testExcluir$userHasPruus() {
        Usuario usuario = new Usuario();
        usuario.setId(1);
        //usuario.setPruus(List.of(new Pruu()));

        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));

        assertThatThrownBy(() -> usuarioService.excluir(1))
                .isInstanceOf(OpomboException.class)
                .hasMessageContaining("Usuário não pode ser excluído, pois já fez um Pruu.");
    }

//    @Test
//    @DisplayName("Deve listar usuários com filtros")
//    public void testListarComFiltros$success() {
//        UsuarioSeletor seletor = new UsuarioSeletor();
//        seletor.setNome("Teste");
//
//        List<Usuario> usuarios = new ArrayList<>();
//        Usuario usuario = new Usuario();
//        usuario.setNome("Carlos Sainz");
//        usuarios.add(usuario);
//
//        when(usuarioRepository.findAll()).thenReturn(usuarios);
//        List<Usuario> result = usuarioService.listarComFiltros(seletor);
//
//        assertThat(result).isNotEmpty();
//        assertThat(result.get(0).getNome()).isEqualTo(usuario.getNome());
//    }

    @Test
    @DisplayName("Deve validar um usuário administrador")
    public void testValidarAdmin$notAdmin() {
        Usuario usuario = new Usuario();
        usuario.setRole(Role.USER);

        assertThatThrownBy(() -> usuarioService.validarAdmin(usuario))
                .isInstanceOf(OpomboException.class)
                .hasMessageContaining("Usuário não autorizado. Somente administradores podem bloquear pruus.");
    }
}

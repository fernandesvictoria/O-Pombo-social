package com.victoria.pombo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.victoria.pombo.model.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Sort;

import com.victoria.pombo.exception.OpomboException;
import com.victoria.pombo.model.dto.DenunciaDTO;
import com.victoria.pombo.model.entity.Denuncia;
import com.victoria.pombo.model.enums.Motivo;
import com.victoria.pombo.model.entity.Usuario;
import com.victoria.pombo.model.repository.DenunciaRepository;
import com.victoria.pombo.model.repository.UsuarioRepository;
import com.victoria.pombo.model.seletor.DenunciaSeletor;

class DenunciaServiceTest {

    @Mock
    private DenunciaRepository denunciaRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private DenunciaService denunciaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void inserirDenunciaComMotivoInvalido() {
        Denuncia denuncia = new Denuncia();
        denuncia.setMotivo(null);

        assertThrows(OpomboException.class, () -> denunciaService.inserir(denuncia), "Motivo inválido");
    }

    @Test
    void inserirDenunciaComMotivoValido() throws OpomboException {
        Denuncia denuncia = new Denuncia();
        denuncia.setMotivo(Motivo.SPAM);

        when(denunciaRepository.save(denuncia)).thenReturn(denuncia);

        Denuncia result = denunciaService.inserir(denuncia);

        assertEquals(denuncia, result);
        verify(denunciaRepository, times(1)).save(denuncia);
    }

    @Test
    void pesquisarTodos() throws OpomboException {
        int userId = 1;
        Usuario admin = new Usuario();
        admin.setId(userId);
        admin.getRole().equals(Role.ADMIN);

        when(usuarioRepository.findById(userId)).thenReturn(Optional.of(admin));
        when(denunciaRepository.findAll()).thenReturn(new ArrayList<>());

        List<Denuncia> result = denunciaService.pesquisarTodos(userId);

        assertNotNull(result);
        verify(denunciaRepository, times(1)).findAll();
    }

    @Test
    void pesquisarPorIdDenunciaExistente() throws OpomboException {
        String denunciaId = UUID.randomUUID().toString();
        Denuncia denuncia = new Denuncia();
        denuncia.setId(denunciaId);

        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setRole(Role.ADMIN); // Defina a propriedade que concede a permissão de admin

        // mock do usuário com permissão de admin
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));
        when(denunciaRepository.findById(denunciaId)).thenReturn(Optional.of(denuncia));

        Denuncia result = denunciaService.pesquisarPorID(denunciaId, 1);

        assertNotNull(result);
        assertEquals(denunciaId, result.getId());
    }

    @Test
    void pesquisarPorIdDenunciaNaoEncontrada() {
        String denunciaId = UUID.randomUUID().toString();

        when(denunciaRepository.findById(denunciaId)).thenReturn(Optional.empty());

        assertThrows(OpomboException.class, () -> denunciaService.pesquisarPorID(denunciaId, 1), "Denúncia não existe.");
    }

    @Test
    void atualizarSituacaoDenuncia() throws OpomboException {
        String denunciaId = UUID.randomUUID().toString();
        Denuncia denuncia = new Denuncia();
        denuncia.setId(denunciaId);
        denuncia.setAnalisada(false);

        when(denunciaRepository.findById(denunciaId)).thenReturn(Optional.of(denuncia));

        denunciaService.atualizarSituacaoDenuncia(denunciaId);

        assertTrue(denuncia.getAnalisada());
        verify(denunciaRepository, times(1)).save(denuncia);
    }

    @Test
    void excluirDenunciaComSucesso() throws OpomboException {
        String denunciaId = UUID.randomUUID().toString();
        Denuncia denuncia = new Denuncia();
        denuncia.setId(denunciaId);

        when(denunciaRepository.findById(denunciaId)).thenReturn(Optional.of(denuncia));

        boolean result = denunciaService.excluir(denunciaId, denunciaId);

        assertTrue(result);
        verify(denunciaRepository, times(1)).deleteById(denunciaId);
    }

    @Test
    void excluirDenunciaUsuarioNaoAutorizado() {
        String denunciaId = UUID.randomUUID().toString();
        Denuncia denuncia = new Denuncia();
        denuncia.setId(denunciaId);

        when(denunciaRepository.findById(denunciaId)).thenReturn(Optional.of(denuncia));

        assertThrows(OpomboException.class, () -> denunciaService.excluir(denunciaId, "anotherUserId"), "Apenas o usuário que realizou a denúncia pode deletá-la.");
    }

    @Test
    void pesquisarComFiltros() throws OpomboException {
        int userId = 1;
        Usuario admin = new Usuario();
        admin.setId(userId);
        admin.setRole(Role.ADMIN);

        DenunciaSeletor seletor = mock(DenunciaSeletor.class);

        when(usuarioRepository.findById(userId)).thenReturn(Optional.of(admin));
        when(denunciaRepository.findAll(seletor, Sort.by(Sort.Direction.DESC, "dataCriacao"))).thenReturn(new ArrayList<>());

        List<Denuncia> result = denunciaService.pesquisarComFiltros(seletor, userId);

        assertNotNull(result);
        verify(denunciaRepository, times(1)).findAll(seletor, Sort.by(Sort.Direction.DESC, "dataCriacao"));
    }

    @Test
    void gerarDenunciaDTO() throws OpomboException {
        String pruuId = UUID.randomUUID().toString();
        int userId = 1;
        List<Denuncia> denuncias = new ArrayList<>();

        Denuncia denuncia1 = new Denuncia();
        denuncia1.setAnalisada(false);
        Denuncia denuncia2 = new Denuncia();
        denuncia2.setAnalisada(true);

        denuncias.add(denuncia1);
        denuncias.add(denuncia2);

        Usuario admin = new Usuario();
        admin.setId(userId);
        admin.setRole(Role.ADMIN);

        when(usuarioRepository.findById(userId)).thenReturn(Optional.of(admin));
        when(denunciaRepository.findByPruuUuid(pruuId)).thenReturn(denuncias);

        DenunciaDTO dto = denunciaService.gerarDTO(userId, pruuId);

        assertNotNull(dto);
        assertEquals(2, dto.getQuantidadeDenuncias());
        assertEquals(1, dto.getQntDenunciasPendentes());
        assertEquals(1, dto.getQntDenunciasAnalisadas());
    }
}

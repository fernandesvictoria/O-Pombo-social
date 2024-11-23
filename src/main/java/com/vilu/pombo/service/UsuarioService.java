package com.vilu.pombo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.vilu.pombo.exeption.PomboException;
import com.vilu.pombo.model.entity.Pruu;
import com.vilu.pombo.model.entity.Usuario;
import com.vilu.pombo.model.repository.PruuRepository;
import com.vilu.pombo.model.repository.UsuarioRepository;
import com.vilu.pombo.model.seletor.UsuarioSeletor;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PruuRepository pruuRepository;

    @Autowired
    private PruuService pruuService;

    public void inserir(Usuario usuario) throws PomboException {
        if (usuarioRepository.existsByCpf(usuario.getCpf())) {
            throw new PomboException("O CPF informado já foi cadastrado no sistema.");
        }
        usuarioRepository.save(usuario);
    }

    public void verificarUsuarioExiste(Usuario usuario) throws PomboException {
        boolean cpfJaCadastrado = usuarioRepository.existsByCpf(usuario.getCpf());
        if (cpfJaCadastrado) {
            throw new PomboException("O CPF informado já foi cadastrado no sistema.");
        }

        boolean emailJaCadastrado = usuarioRepository.existsByEmailIgnoreCase(usuario.getEmail());
        if (emailJaCadastrado) {
            throw new PomboException("O email informado já foi cadastrado no sistema.");
        }
    }

    public Usuario atualizar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public void excluir(String idUsuario) throws PomboException {
        List<Pruu> pruusUsuario = pruuService.listarPruusPorIdUsuario(idUsuario);

        if (!pruusUsuario.isEmpty()) {
            throw new PomboException("Usuários com pruus postados não podem ser deletados.");
        }

        usuarioRepository.deleteById(idUsuario);
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public Usuario pesquisarPorId(String id) throws PomboException {
        return usuarioRepository.findById(id).orElseThrow(() -> new PomboException("Usuário não encontrado!"));
    }

    public List<Usuario> pesquisarComSeletor(UsuarioSeletor seletor) {
        if (seletor.hasPaginacao()) {
            int numeroPagina = seletor.getPagina();
            int tamanhoPagina = seletor.getLimite();

            PageRequest pagina = PageRequest.of(numeroPagina - 1, tamanhoPagina);
            return usuarioRepository.findAll(pagina).toList();
        }

        return usuarioRepository.findAll();
    }

    @Transactional
    public void curtir(String idUsuario, String idPruu) throws PomboException {
        Usuario usuario = usuarioRepository.findById(idUsuario).orElseThrow(() -> new PomboException("Usuário não encontrado."));

        Pruu pruu = pruuRepository.findById(idPruu).orElseThrow(() -> new PomboException("Pruu não encontrado."));

        boolean usuarioJaCurtiu = verificarUsuarioCurtiu(idPruu, idUsuario);

        if (usuarioJaCurtiu) {
            pruu.setQuantidadeLikes(pruu.getQuantidadeLikes() - 1);
            usuario.getPruusCurtidos().remove(pruu);
        } else {
            pruu.setQuantidadeLikes(pruu.getQuantidadeLikes() + 1);
            usuario.getPruusCurtidos().add(pruu);
        }

        usuarioRepository.save(usuario);
    }

    private boolean verificarUsuarioCurtiu(String idPruu, String idUsuario) {
        return pruuRepository.existsByUuidAndUsuariosQueCurtiram_Uuid(idPruu, idUsuario);
    }

    public List<Usuario> pesquisarUsuariosQueCurtiram(String idPruu) throws PomboException {
        return pruuRepository.findById(idPruu).map(Pruu::getUsuariosQueCurtiram).map(ArrayList::new).orElseThrow(() -> new PomboException("Pruu não encontrado."));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return usuarioRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado!"));
    }
}
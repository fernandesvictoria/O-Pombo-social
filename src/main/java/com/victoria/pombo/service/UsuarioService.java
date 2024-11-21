package com.victoria.pombo.service;

import com.victoria.pombo.exception.OpomboException;
import com.victoria.pombo.model.entity.Usuario;
import com.victoria.pombo.model.enums.Role;
import com.victoria.pombo.model.repository.UsuarioRepository;
import com.victoria.pombo.model.seletor.UsuarioSeletor;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UsuarioService implements UserDetailsService {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario inserir(Usuario novoUsuario) throws OpomboException {
        validarPerfilUsuario(novoUsuario);
        return repository.save(novoUsuario);
    }

    private void validarPerfilUsuario(Usuario usuario) {
        if (usuario.getRole() == null) {
            usuario.setRole(Role.USER);
        }
    }

    public List<Usuario> pesquisarTodos() {
        return repository.findAll();
    }

    public Usuario pesquisarPorId(int id) throws OpomboException {
        return repository.findById(id).orElseThrow(() -> new OpomboException("Usuário não encontrado."));
    }

    public Usuario atualizar(Usuario usuarioAtualizado) throws OpomboException {

        if (usuarioAtualizado.getId() == null) {
            throw new OpomboException("Informe o ID");
        }

        return repository.save(usuarioAtualizado);
    }

    public void excluir(Integer id) throws OpomboException {
        Usuario usuario = repository.findById(id).orElseThrow(() -> new OpomboException("Usuário não encontrado."));

        // verificar se o usuário já fez algum Pruu
//        if (usuario.getPruus() != null && !usuario.getPruus().isEmpty()) {
//            throw new OpomboException("Usuário não pode ser excluído, pois já fez um Pruu.");
//        }

        repository.deleteById(id);
    }

    public void validarAdmin(Usuario usuario) throws OpomboException {
        if (!usuario.getRole().equals(Role.ADMIN)) {
            throw new OpomboException("Usuário não autorizado. Somente administradores podem bloquear pruus.");
        }
    }

    public List<Usuario> listarComFiltros(UsuarioSeletor seletor) {
        Specification<Usuario> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (seletor.getNome() != null && !seletor.getNome().trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("nome"), "%" + seletor.getNome() + "%"));
            }

            if (seletor.getEmail() != null && !seletor.getEmail().trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("email"), "%" + seletor.getEmail() + "%"));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        return repository.findAll(specification);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Usuário " + username + " não encontrado"));
    }
}

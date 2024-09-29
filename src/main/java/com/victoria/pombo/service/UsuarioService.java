package com.victoria.pombo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import com.victoria.pombo.exception.OpomboException;
import com.victoria.pombo.model.entity.Usuario;
import com.victoria.pombo.model.repository.UsuarioRepository;
import com.victoria.pombo.model.seletor.UsuarioSeletor;

import jakarta.persistence.criteria.Predicate;

import java.util.ArrayList;
import java.util.List;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository repository;

	public Usuario inserir(Usuario novoUsuario) throws OpomboException {
		return repository.save(novoUsuario);
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
		if (usuario.getPruus() != null && !usuario.getPruus().isEmpty()) {
			throw new OpomboException("Usuário não pode ser excluído, pois já fez um Pruu.");
		}

		repository.deleteById(id);
	}

	public void validarAdmin(Usuario usuario) throws OpomboException {
		if (!usuario.isAdmin()) {
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

}

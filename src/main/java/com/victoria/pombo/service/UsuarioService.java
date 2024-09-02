package com.victoria.pombo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.victoria.pombo.exception.OpomboException;
import com.victoria.pombo.model.entity.Usuario;
import com.victoria.pombo.model.repository.UsuarioRepository;

import java.util.List;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository repository;

	public Usuario inserir(Usuario novoUsuario) {
		// validarPerfilJogador(novoJogador);
		return repository.save(novoUsuario);
	}

	public List<Usuario> pesquisarTodos() {
		return repository.findAll();
	}

	public Usuario pesquisarPorId(int id) {
		return repository.findById(id).get();
	}

	public Usuario atualizar(Usuario usuarioAtualizado) throws OpomboException {

		if (usuarioAtualizado.getId() == null) {
			throw new OpomboException("Informe o ID");
		}

		return repository.save(usuarioAtualizado);
	}

	public void excluir(Integer id) {
		repository.deleteById(id);
	}
	
}

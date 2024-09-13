package com.victoria.pombo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.victoria.pombo.exception.OpomboException;
import com.victoria.pombo.model.entity.Usuario;
import com.victoria.pombo.model.repository.UsuarioRepository;
import com.victoria.pombo.model.seletor.UsuarioSeletor;

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

	public void excluir(Integer id) {
		repository.deleteById(id);
	}
	
	public void validarAdmin(Usuario usuario) throws OpomboException {
	    if (!usuario.isAdmin()) {
	        throw new OpomboException("Usuário não autorizado. Somente administradores podem bloquear pruus.");
	    }
	}

//	public List<Usuario> listarComSeletor(UsuarioSeletor seletor) {
//		if(seletor.temPaginacao()) {
//			int pageNumber = seletor.getPagina();
//			int pageSize = seletor.getLimite();
//			
//		
//			PageRequest pagina = PageRequest.of(pageNumber - 1, pageSize);
//			return repository.findAll(seletor, pagina).toList();
//		}
//		
//		//https://www.baeldung.com/spring-data-jpa-query-by-example
//		return repository.findAll(seletor);
//	}


}

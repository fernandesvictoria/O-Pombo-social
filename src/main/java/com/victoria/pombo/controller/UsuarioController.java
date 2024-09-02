package com.victoria.pombo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

import com.victoria.pombo.exception.OpomboException;
import com.victoria.pombo.model.entity.Usuario;
import com.victoria.pombo.service.UsuarioService;

@RestController
@RequestMapping(path = "/usuario")
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;

	@GetMapping
	public List<Usuario> pesquisarTodos(){
		List<Usuario> usuarios = usuarioService.pesquisarTodos();
		
		return usuarios;
	}
	
	@GetMapping(path = "/{id}")
	public Usuario pesquisarPorId(@PathVariable int id) {
		return usuarioService.pesquisarPorId(id);
	}
	
	@PostMapping
	public Usuario inserir(@RequestBody Usuario novoUsuario) {
		return usuarioService.inserir(novoUsuario);
	}
	
	@PutMapping
	public Usuario atualizar(@RequestBody Usuario novoUsuario) throws OpomboException {
		return usuarioService.atualizar(novoUsuario);
	}
	
	@DeleteMapping(path = "/{id}")
    public void excluir(@PathVariable int id) {
        usuarioService.excluir(id);
    }
}

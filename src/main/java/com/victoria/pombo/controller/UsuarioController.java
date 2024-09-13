package com.victoria.pombo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.victoria.pombo.model.entity.Pruu;
import com.victoria.pombo.model.entity.Usuario;
import com.victoria.pombo.model.seletor.UsuarioSeletor;
import com.victoria.pombo.service.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/usuario")

public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;

	@Operation(summary = "Listar todos os usuários", description = "Retorna uma lista de todos os usuários cadastrados no sistema.", responses = {
			@ApiResponse(responseCode = "200", description = "Lista de usuários retornada com sucesso") })
	@GetMapping
	public List<Usuario> pesquisarTodos() {
		List<Usuario> usuarios = usuarioService.pesquisarTodos();

		return usuarios;
	}

	@Operation(summary = "Pesquisar usuário por ID", description = "Busca um usuário específico pelo seu ID.")
	@GetMapping(path = "/{id}")
	public Usuario pesquisarPorId(@PathVariable int id) throws OpomboException {
		return usuarioService.pesquisarPorId(id);
	}
	
//	@Operation(summary = "Pesquisar usuário com filtro", 
//			   description = "Retorna uma lista de usuários que atendem aos critérios especificados no seletor.")
//	@PostMapping("/filtro")
//	public List<Usuario> pesquisarComSeletor(@RequestBody UsuarioSeletor seletor) {
//		return usuarioService.listarComSeletor(seletor);
//	}

	@Operation(summary = "Inserir novo usuário", description = "Adiciona um novo usuário ao sistema.", responses = {
			@ApiResponse(responseCode = "201", description = "Usuário criado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Usuario.class))),
			@ApiResponse(responseCode = "400", description = "Erro de validação ou regra de negócio", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"message\": \"Erro de validação: campo X é obrigatório\", \"status\": 400}"))) })
	@PostMapping
	public ResponseEntity<Usuario> inserir(@Valid @RequestBody Usuario novoUsuario) {
		try {
			Usuario usuarioSalvo = usuarioService.inserir(novoUsuario);
			return new ResponseEntity(usuarioSalvo, HttpStatus.CREATED);
		} catch (OpomboException e) {
			return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@Operation(summary = "Atualizar usuário existente", description = "Atualiza os dados de um usuário existente.")
	@PutMapping
	public Usuario atualizar(@RequestBody Usuario novoUsuario) throws OpomboException {
		return usuarioService.atualizar(novoUsuario);
	}

	@Operation(summary = "Deletar usuário por ID", description = "Remove um usuário específico pelo seu ID.", responses = {
			@ApiResponse(responseCode = "204", description = "Usuário excluído com sucesso"), })
	@DeleteMapping(path = "/{id}")
	public void excluir(@PathVariable int id) {
		usuarioService.excluir(id);
	}
	
	
}

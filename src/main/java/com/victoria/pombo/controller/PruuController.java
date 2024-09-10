package com.victoria.pombo.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.victoria.pombo.exception.OpomboException;
import com.victoria.pombo.model.entity.Pruu;
import com.victoria.pombo.service.PruuService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ExampleObject;

@RestController
@RequestMapping(path = "/pruu")
public class PruuController {

	@Autowired
	private PruuService pruuService;

	@Operation(summary = "Listar todos os pruus", description = "Retorna uma lista de todos os pruus cadastrados no sistema.", responses = {
			@ApiResponse(responseCode = "200", description = "Lista de pruus retornada com sucesso") })

	@GetMapping
	public List<Pruu> pesquisarTodos() {
		List<Pruu> pruus = pruuService.pesquisarTodos();

		return pruus;
	}

	@Operation(summary = "Pesquisar pruu por ID", description = "Busca um pruu específico pelo seu ID.")

	@GetMapping(path = "/{id}")
	public Optional<Pruu> pesquisarPorId(@PathVariable UUID id) throws OpomboException {
		return pruuService.pesquisarPorId(id);
	}

	@Operation(summary = "Inserir novo pruu", description = "Adiciona um novo pruu ao sistema.", responses = {
			@ApiResponse(responseCode = "201", description = "Pruu criado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Pruu.class))),
			@ApiResponse(responseCode = "400", description = "Erro de validação ou regra de negócio", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"message\": \"Erro de validação: campo X é obrigatório\", \"status\": 400}"))) })

	@PostMapping
	public Pruu inserir(@RequestBody Pruu novoPruu) {
		return pruuService.inserir(novoPruu);
	}

	@Operation(summary = "Deletar pruu por ID", description = "Remove um pruu específico pelo seu ID.")
	@DeleteMapping(path = "/{id}")
	public void excluir(@PathVariable UUID id) {
		pruuService.excluir(id);
	}

	@Operation(summary = "Curtir um Pruu")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Pruu curtido com sucesso"),
			@ApiResponse(responseCode = "404", description = "Pruu não encontrado"),
			@ApiResponse(responseCode = "500", description = "Erro interno do servidor") })
	@PostMapping("/{id}/curtir")
	public ResponseEntity<Pruu> curtirPruu(@PathVariable UUID id,@RequestParam Integer usuarioId) throws OpomboException {
		Pruu pruuCurtido = pruuService.curtirPruu(id, usuarioId);
		return ResponseEntity.ok(pruuCurtido);
	}

	// pesquisar pruus de um usuario por ordem decrescente.
	@Operation(summary = "Listar todas as Pruus", description = "Retorna uma lista de todas as Pruus ordenadas pela data de criação em ordem decrescente.", responses = {
			@ApiResponse(responseCode = "200", description = "Lista de Pruus retornada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Pruu.class))),
			@ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(mediaType = "application/json", schema = @Schema(description = "Detalhes do erro interno", example = "{\"message\": \"Erro interno do servidor\", \"status\": 500}"))) })
	@GetMapping("/all")
	public ResponseEntity<List<Pruu>> pesquisarTodosPruusPorOrdemDesc() {
		List<Pruu> pruus = pruuService.findAllOrderByCreatedAtDesc();
		return new ResponseEntity<>(pruus, HttpStatus.OK);
	}

}

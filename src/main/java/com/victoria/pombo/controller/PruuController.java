package com.victoria.pombo.controller;

import java.util.List;
import java.util.Optional;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.victoria.pombo.exception.OpomboException;
import com.victoria.pombo.model.entity.Pruu;
import com.victoria.pombo.model.entity.Usuario;
import com.victoria.pombo.model.seletor.PruuSeletor;
import com.victoria.pombo.service.PruuService;
import com.victoria.pombo.service.UsuarioService;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ExampleObject;

@RestController
@RequestMapping(path = "/pruu")
public class PruuController {

	@Autowired
	private PruuService pruuService;
	@Autowired
	private UsuarioService usuarioService;

	@Operation(summary = "Listar todos os pruus", description = "Retorna uma lista de todos os pruus cadastrados no sistema.", responses = {
			@ApiResponse(responseCode = "200", description = "Lista de pruus retornada com sucesso") })

	@GetMapping
	public List<Pruu> pesquisarTodos() {
		List<Pruu> pruus = pruuService.pesquisarTodos();

		return pruus;
	}

	@Operation(summary = "Pesquisar pruu por ID", description = "Busca um pruu específico pelo seu ID.")

	@GetMapping(path = "/{id}")
	public Optional<Pruu> pesquisarPorId(@PathVariable String id) throws OpomboException {
		return pruuService.pesquisarPorId(id);
	}

	@Operation(summary = "Inserir novo pruu", description = "Adiciona um novo pruu ao sistema.", responses = {
			@ApiResponse(responseCode = "200", description = "Pruu criado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Pruu.class))),
			@ApiResponse(responseCode = "400", description = "Erro de validação ou regra de negócio", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"message\": \"Erro de validação: campo X é obrigatório\", \"status\": 400}"))) })
	@PostMapping
	public ResponseEntity<?> inserir(@RequestBody Pruu pruu) throws OpomboException {
		pruuService.inserir(pruu);
		return ResponseEntity.ok().build();
	}

	// (Não exposto)
//	@Operation(summary = "Deletar pruu por ID", description = "Remove um pruu específico pelo seu ID.", responses = {
//			@ApiResponse(responseCode = "204", description = "Pruu excluido com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Pruu.class))) })
//	@DeleteMapping(path = "/{id}")
//	public ResponseEntity<Void> excluir(@PathVariable String id) {
//		pruuService.excluir(id);
//		return ResponseEntity.noContent().build();
//	}

	@Operation(summary = "Curtir um Pruu")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Pruu curtido com sucesso"),
			@ApiResponse(responseCode = "404", description = "Pruu não encontrado"),
			@ApiResponse(responseCode = "500", description = "Erro interno do servidor") })
	@PostMapping("/{id}/curtir")
	public ResponseEntity<Pruu> curtirPruu(@PathVariable String id, @RequestParam Integer usuarioId)
			throws OpomboException {
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

	@Operation(summary = "Bloquear um Pruu", description = "Bloqueia um Pruu específico se o usuário for um administrador.")
	@PostMapping("/bloquear/{id}")
	public ResponseEntity<String> bloquearPruu(@PathVariable String id, @RequestBody Usuario usuario) {
		try {
			usuarioService.validarAdmin(usuario);

			pruuService.bloquear(id);

			return new ResponseEntity<>("Pruu bloqueado com sucesso.", HttpStatus.OK);
		} catch (OpomboException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
		}
	}
	@Operation(summary = "Pesquisar com filtro", description = "Retorna uma lista de pruus seguinte especificações.", responses = {
			@ApiResponse(responseCode = "200", description = "Pruus filtrados com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Pruu.class))),
			@ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(mediaType = "application/json", schema = @Schema(description = "Detalhes do erro interno", example = "{\"message\": \"Erro interno do servidor\", \"status\": 500}"))) })
	@GetMapping("/filtro")
	public ResponseEntity<List<Pruu>> pesquisarComFiltros(PruuSeletor seletor) {
		List<Pruu> pruus;

		try {
			pruus = pruuService.pesquisarComFiltros(seletor);
			if (pruus.isEmpty()) {
				throw new OpomboException("Nenhum Pruu encontrado com os filtros fornecidos.");
			}
		} catch (OpomboException e) {
			return ResponseEntity.badRequest().body(null); //400 Bad Request
		} catch (Exception e) {
			return ResponseEntity.status(500).body(null); //500 Internal Server Error
		}

		return ResponseEntity.ok(pruus); //200 
	}
}

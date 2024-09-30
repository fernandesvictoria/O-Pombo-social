package com.victoria.pombo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.victoria.pombo.exception.OpomboException;
import com.victoria.pombo.model.dto.DenunciaDTO;
import com.victoria.pombo.model.entity.Denuncia;
import com.victoria.pombo.model.seletor.DenunciaSeletor;
import com.victoria.pombo.service.DenunciaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping(path = "/denuncia")
public class DenunciaController {

	@Autowired
	private DenunciaService denunciaService;

	@Operation(summary = "Inserir nova denuncia", description = "Cria uma nova denuncia", responses = {
			@ApiResponse(responseCode = "200", description = "Denuncia registrada com sucesso"), })
	@PostMapping
	public Denuncia inserir(@RequestBody Denuncia denuncia) throws OpomboException {
		// tratar erros ainda
		return denunciaService.inserir(denuncia);
	}

	@Operation(summary = "Pesquisar todas denuncias", description = "Retorna lista de denuncias para o admin.", responses = {
			@ApiResponse(responseCode = "200", description = "Lista de denuncias retornada com sucesso."),
			@ApiResponse(responseCode = "401", description = "Usuário não autorizado") })
	@GetMapping
	public List<Denuncia> pesquisarTodos(@RequestParam Integer adminId) throws OpomboException {
		return denunciaService.pesquisarTodos(adminId);
	}

	@Operation(summary = "Pesquisar denuncia por id", description = "Retorna denuncia especifica para o admin.", responses = {
			@ApiResponse(responseCode = "200", description = "Denuncia retornada com sucesso."),
			@ApiResponse(responseCode = "400", description = "Denuncia não encontrada."),
			@ApiResponse(responseCode = "401", description = "Usuário não autorizado.") })
	@GetMapping("/{id}")
	public Denuncia pesquisarPorID(@PathVariable String id, @RequestParam Integer adminId) throws OpomboException {
		return denunciaService.pesquisarPorID(id, adminId);
	}

	@Operation(summary = "Listar denuncias", description = "Lista denuncias de acordo com os filtros selecionados.", responses = {
			@ApiResponse(responseCode = "200", description = "Lista filtrada com sucesso."),
			@ApiResponse(responseCode = "401", description = "Usuário não autorizado.") })
	@PostMapping("/filtro")
	public List<Denuncia> pesquisarComFiltros(@RequestBody DenunciaSeletor seletor, @RequestParam Integer adminID)
			throws OpomboException {
		return denunciaService.pesquisarComFiltros(seletor, adminID);
	}

	@Operation(summary = "Deleta uma denuncia", description = "Deleta uma denuncia.", responses = {
			@ApiResponse(responseCode = "200", description = "Denuncia excluida com sucesso."),
			@ApiResponse(responseCode = "400", description = "Denuncia não encontrada"),
			@ApiResponse(responseCode = "401", description = "Usuário não atualizado.") })
	@DeleteMapping("/{id}")
	public boolean deleteById(@PathVariable String id, String idUsuarioAtual) throws OpomboException {

		return denunciaService.excluir(id, idUsuarioAtual);
	}

	@Operation(summary = "Retorna relatório de denuncia", description = "Retorna relatório de denuncia de um pruu.", responses = {
			@ApiResponse(responseCode = "200", description = "Relatório retornado com sucesso."),
			@ApiResponse(responseCode = "400", description = "Denuncia não encontrada."),
			@ApiResponse(responseCode = "401", description = "Usuário não autorizado.") })
	@GetMapping("/relatorio")
	public DenunciaDTO gerarRelatorioDTO(@RequestParam Integer adminID, @RequestParam String pruuID)
			throws OpomboException {
		return denunciaService.gerarDTO(adminID, pruuID);
	}

}

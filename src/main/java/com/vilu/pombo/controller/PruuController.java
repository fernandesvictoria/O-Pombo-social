package com.vilu.pombo.controller;

import com.vilu.pombo.auth.AuthService;
import com.vilu.pombo.exeption.PomboException;
import com.vilu.pombo.model.dto.PruuDTO;
import com.vilu.pombo.model.entity.Pruu;
import com.vilu.pombo.model.entity.Usuario;
import com.vilu.pombo.model.enums.Perfil;
import com.vilu.pombo.model.seletor.PruuSeletor;
import com.vilu.pombo.service.PruuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/pruu")
@CrossOrigin(origins = {"http://localhost:4200"}, maxAge = 3600)
public class PruuController {

    @Autowired
    private PruuService pruuService;
    @Autowired
    private AuthService authService;

    @Operation(summary = "Upload de imagem para Pruu", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Arquivo de imagem a ser enviado", required = true, content = @Content(mediaType = "multipart/form-data", schema = @Schema(type = "string", format = "binary"))), description = "Realiza o upload de uma imagem associada a um pruu.")
    @PostMapping("/{id}/upload")
    public void fazerUploadImagemPruu(@RequestParam("imagem") MultipartFile imagem, @PathVariable Integer id) throws PomboException, IOException {
        if (imagem == null) {
            throw new PomboException("Arquivo inválido");
        }

        Usuario usuarioAutenticado = authService.getUsuarioAutenticado();
        if (usuarioAutenticado == null) {
            throw new PomboException("Usuário não encontrado");
        }
        if (usuarioAutenticado.getPerfil() == Perfil.USUARIO) {
            throw new PomboException("Usuário sem permissão de acesso");
        }
        pruuService.salvarImagemPruu(imagem, String.valueOf(id));
    }

    @Operation(summary = "Inserir novo pruu", description = "Adiciona um novo pruu ao sistema.", responses = {@ApiResponse(responseCode = "200", description = "Pruu criado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Pruu.class))), @ApiResponse(responseCode = "400", description = "Erro de validação ou regra de negócio", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"message\": \"Erro de validação: campo X é obrigatório\", \"status\": 400}")))})
    @PostMapping
    public ResponseEntity<Pruu> inserir(@Valid @RequestBody Pruu pruuEnviado) throws PomboException {
        return ResponseEntity.ok(pruuService.inserir(pruuEnviado));
    }

    @Operation(summary = "Listar todos os pruus", description = "Retorna uma lista de todos os pruus cadastrados no sistema.", responses = {@ApiResponse(responseCode = "200", description = "Lista de pruus retornada com sucesso")})

    @GetMapping
    public List<Pruu> listarTodos() {
        return pruuService.listarTodos();
    }

    @Operation(summary = "Pesquisar com filtro", description = "Retorna uma lista de pruus seguinte especificações.", responses = {@ApiResponse(responseCode = "200", description = "Pruus filtrados com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Pruu.class))), @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(mediaType = "application/json", schema = @Schema(description = "Detalhes do erro interno", example = "{\"message\": \"Erro interno do servidor\", \"status\": 500}")))})
    @GetMapping("/filtro")
    public List<Pruu> pesquisarComSeletor(@RequestBody PruuSeletor seletor) {
        return pruuService.pesquisarComSeletor(seletor);
    }

    @Operation(summary = "Listar todos os pruus de um usuário", description = "Retorna uma lista de todos os pruus de um usuário cadastrado no sistema.", responses = {@ApiResponse(responseCode = "200", description = "Lista de pruus retornada com sucesso")})
    @GetMapping(path = "/usuario/{idUsuario}")
    public List<Pruu> listarTodosUsuarios(@PathVariable String idUsuario) throws PomboException {
        return pruuService.listarPruusPorIdUsuario(idUsuario);
    }

    @Operation(summary = "Pesquisar pruu por ID", description = "Busca um pruu específico pelo seu ID.")
    @GetMapping(path = "/{id}")
    public ResponseEntity<Optional<Pruu>> pesquisarPorId(@PathVariable String id) throws PomboException {
        Optional<Pruu> pruu = pruuService.pesquisarPorId(id);
        return ResponseEntity.ok(pruu);
    }

    @Operation(summary = "Gera relatório de pruu",
            description = "Retorna relatorio de pruus",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Relatório gerado com sucesso."),
                    @ApiResponse(responseCode = "400", description = "Pruu não encontrado."),
            })
    @GetMapping("/relatorio/{idPruu}")
    public ResponseEntity<PruuDTO> pesquisarRelatorioPruu(@PathVariable String idPruu) throws PomboException {
        PruuDTO dto = pruuService.gerarRelatorioPruu(idPruu);
        return ResponseEntity.ok(dto);
    }

//    @Operation(summary = "Atualizar pruu", description = "Atualiza os dados de um pruu.")
//    @PutMapping
//    public ResponseEntity<Pruu> atualizarPruu(@Valid @RequestBody Pruu pruu) throws PomboException {
//        return ResponseEntity.ok(pruuService.atualizar(pruu));
//    }

    @Operation(summary = "Deletar pruu", description = "Exclui um pruu pelo seu ID.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable String id) throws PomboException {
        pruuService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Bloquear um pru", description = "Bloqueia um pruu pelo seu ID.")
    @PutMapping("/{idUsuario}/bloquear/{idPruu}")
    public ResponseEntity<Pruu> bloquearPruu(@PathVariable String idPruu, @PathVariable String idUsuario) throws PomboException {
        return ResponseEntity.ok(pruuService.bloquear(idPruu, idUsuario));
    }

}
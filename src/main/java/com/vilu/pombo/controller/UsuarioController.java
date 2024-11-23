package com.vilu.pombo.controller;

import com.vilu.pombo.exeption.PomboException;
import com.vilu.pombo.model.entity.Usuario;
import com.vilu.pombo.model.seletor.UsuarioSeletor;
import com.vilu.pombo.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/usuario")
@CrossOrigin(origins = {"http://localhost:4200"}, maxAge = 3600)
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Operation(summary = "Listar todos os usuários", description = "Retorna uma lista de todos os usuários cadastrados no sistema.", responses = {@ApiResponse(responseCode = "200", description = "Lista de usuários retornada com sucesso")})
    @GetMapping
    public List<Usuario> listarTodos() {
        return usuarioService.listarTodos();
    }

    @Operation(summary = "Pesquisar usuário por ID", description = "Busca um usuário específico pelo seu ID.")
    @GetMapping(path = "/{id}")
    public ResponseEntity<Usuario> pesquisarPorId(@PathVariable String id) throws PomboException {
        Usuario usuario = usuarioService.pesquisarPorId(id);
        return ResponseEntity.ok(usuario);
    }

    @Operation(summary = "Pesquisar com filtro", description = "Retorna uma lista de usuários de acordo com o filtro selecionado.")
    @PostMapping("/filtro")
    public List<Usuario> pesquisarComSeletor(@RequestBody UsuarioSeletor seletor) {
        return usuarioService.pesquisarComSeletor(seletor);
    }

    @Operation(summary = "Atualizar usuário existente", description = "Atualiza os dados de um usuário existente.")
    @PutMapping
    public ResponseEntity<Usuario> atualizar(@Valid @RequestBody Usuario usuarioAlterado) throws PomboException {
        return ResponseEntity.ok(usuarioService.atualizar(usuarioAlterado));
    }

    @Operation(summary = "Deletar usuário por ID", description = "Remove um usuário específico pelo seu ID.", responses = {@ApiResponse(responseCode = "200", description = "Usuário excluído com sucesso"),})
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> excluir(@PathVariable String id) throws PomboException {
        usuarioService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Curtir um Pruu", description = "Adiciona um like a um Pruu específico.", responses = {@ApiResponse(responseCode = "200", description = "Pruu curtido com sucesso."), @ApiResponse(responseCode = "404", description = "Houve um erro ao curtir o Pruu.")})
    @PostMapping("/{idUsuario}/curtir/{idPruu}")
    public ResponseEntity<String> curtir(@PathVariable String idUsuario, @PathVariable String idPruu) throws PomboException {
        try {
            usuarioService.curtir(idUsuario, idPruu);
            return ResponseEntity.ok("Pruu curtido com sucesso.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @Operation(summary = "Pesquisar usuários que curtiram um Pruu", description = "Retorna uma lista de usuários que curtiram um Pruu específico.", responses = {@ApiResponse(responseCode = "200", description = "Lista de usuários que curtiram o Pruu retornada com sucesso.", content = @Content(schema = @Schema(implementation = Usuario.class)))})
    @GetMapping(path = "/{idPruu}/curtidas")
    public List<Usuario> pesquisarUsuariosQueCurtiram(@PathVariable String idPruu) throws PomboException {
        return usuarioService.pesquisarUsuariosQueCurtiram(idPruu);
    }


}
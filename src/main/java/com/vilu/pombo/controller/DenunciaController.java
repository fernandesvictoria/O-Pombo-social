package com.vilu.pombo.controller;

import com.vilu.pombo.exeption.PomboException;
import com.vilu.pombo.model.dto.DenunciaDTO;
import com.vilu.pombo.model.entity.Denuncia;
import com.vilu.pombo.service.DenunciaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/denuncias")
@CrossOrigin(origins = {"http://localhost:4200"}, maxAge = 3600)
public class DenunciaController {

    @Autowired
    private DenunciaService denunciaService;

    @Operation(summary = "Inserir nova denuncia", description = "Cria uma nova denuncia", responses = {@ApiResponse(responseCode = "200", description = "Denuncia registrada com sucesso"),})
    @PostMapping
    public ResponseEntity<Denuncia> denunciar(@Valid @RequestBody Denuncia denuncia) throws PomboException {
        return ResponseEntity.ok(denunciaService.denunciar(denuncia));
    }

    @Operation(summary = "Pesquisar todas denuncias", description = "Retorna lista de denuncias para o admin.", responses = {@ApiResponse(responseCode = "200", description = "Lista de denuncias retornada com sucesso."), @ApiResponse(responseCode = "401", description = "Usuário não autorizado")})
    @GetMapping
    public List<Denuncia> listarDenuncias() {
        return denunciaService.listarDenuncias();
    }

    @Operation(summary = "Pesquisar denuncia por id", description = "Retorna denuncia especifica para o admin.", responses = {@ApiResponse(responseCode = "200", description = "Denuncia retornada com sucesso."), @ApiResponse(responseCode = "400", description = "Denuncia não encontrada."), @ApiResponse(responseCode = "401", description = "Usuário não autorizado.")})
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Denuncia>> pesquisarDenunciaPorId(@PathVariable String id) throws PomboException {
        Optional<Denuncia> denuncia = denunciaService.pesquisarDenunciaPorId(id);
        return ResponseEntity.ok(denuncia);
    }

    @Operation(summary = "Retorna relatório de denuncia", description = "Retorna relatório de denuncia de um pruu.", responses = {@ApiResponse(responseCode = "200", description = "Relatório retornado com sucesso."), @ApiResponse(responseCode = "400", description = "Denuncia não encontrada."), @ApiResponse(responseCode = "401", description = "Usuário não autorizado.")})
    @GetMapping(path = "/relatorio/{idPruu}")
    public ResponseEntity<DenunciaDTO> pesquisarRelatorioPruu(@PathVariable String idPruu) throws PomboException {
        DenunciaDTO dto = denunciaService.gerarRelatorioPorIdPruu(idPruu);
        return ResponseEntity.ok(dto);
    }

}
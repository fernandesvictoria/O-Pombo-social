package com.victoria.pombo.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.victoria.pombo.exception.OpomboException;
import com.victoria.pombo.model.entity.Pruu;
import com.victoria.pombo.service.PruuService;

@RestController
@RequestMapping(path = "/pruu")
public class PruuController {

	@Autowired
	private PruuService service;
	

	@GetMapping
	public List<Pruu> pesquisarTodos(){
		List<Pruu> pruus = service.pesquisarTodos();
		
		return pruus;
	}
	
	@GetMapping(path = "/{id}")
	public Optional<Pruu> pesquisarPorId(@PathVariable UUID id) throws OpomboException {
		return service.pesquisarPorId(id);
	}
	
	@PostMapping
	public Pruu inserir(@RequestBody Pruu novoPruu) {
		return service.inserir(novoPruu);
	}
	
	
	
	@DeleteMapping(path = "/{id}")
    public void excluir(@PathVariable UUID id) {
        service.excluir(id);
    }

}

package com.victoria.pombo.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.victoria.pombo.exception.OpomboException;
import com.victoria.pombo.model.entity.Pruu;
import com.victoria.pombo.model.repository.PruuRepository;

@Service
public class PruuService {

	@Autowired
	private PruuRepository repository;

	public Pruu inserir(Pruu novoPruu) {

		return repository.save(novoPruu);
	}

	public List<Pruu> pesquisarTodos() {
		return repository.findAll();
	}

	public Optional<Pruu> pesquisarPorId(UUID id) throws OpomboException {
		Optional<Pruu> pruu = repository.findById(id);
		if (pruu.isPresent()) {
            return pruu;
        } else {
            throw new OpomboException("Pruu  ID " + id + " não encontrado.");
        }
    }

	public void excluir(UUID id) {
		repository.deleteById(id);
	}
}

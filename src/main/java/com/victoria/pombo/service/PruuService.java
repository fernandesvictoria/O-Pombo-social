package com.victoria.pombo.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.victoria.pombo.exception.OpomboException;
import com.victoria.pombo.model.entity.Pruu;
import com.victoria.pombo.model.entity.Usuario;
import com.victoria.pombo.model.repository.PruuRepository;
import com.victoria.pombo.model.repository.UsuarioRepository;

@Service
public class PruuService {

	@Autowired
	private PruuRepository pruuRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	public void inserir(Pruu pruu) throws OpomboException {
		Usuario usuario = usuarioRepository.findById(pruu.getUsuario().getId())
				.orElseThrow(() -> new OpomboException("Usuário não encontrado"));

		pruu.setUsuario(usuario);
		pruuRepository.save(pruu);
	}

	public List<Pruu> pesquisarTodos() {
		return pruuRepository.findAll();
	}

	public Optional<Pruu> pesquisarPorId(UUID id) throws OpomboException {
		Optional<Pruu> pruu = pruuRepository.findById(id);
		if (pruu.isPresent()) {
			return pruu;
		} else {
			throw new OpomboException("Pruu  ID " + id + " não encontrado.");
		}
	}

	public void excluir(UUID id) {
		pruuRepository.deleteById(id);
	}

	public Pruu curtirPruu(UUID pruuId, Integer usuarioId) throws OpomboException {
	    Pruu pruu = pruuRepository.findById(pruuId)
	            .orElseThrow(() -> new OpomboException("Pruu não encontrado"));

	    Usuario usuario = usuarioRepository.findById(usuarioId)
	            .orElseThrow(() -> new OpomboException("Usuário não encontrado"));

	    // Verifica se o usuário já curtiu o Pruu
	    if (pruu.getLikes().contains(usuario)) {
	        // Se já curtiu, remove o like e decrementa o contador
	        pruu.setQuantidadeLikes(pruu.getQuantidadeLikes() - 1);
	        pruu.getLikes().remove(usuario);
	    } else {
	        // Se não curtiu, adiciona o like e incrementa o contador
	        pruu.setQuantidadeLikes(pruu.getQuantidadeLikes() + 1);
	        pruu.getLikes().add(usuario);
	    }

	    // Salva o estado atualizado do Pruu
	    return pruuRepository.save(pruu);
	}


	public List<Pruu> findAllByUserOrderByCreatedAtDesc(Usuario usuario) {
		return pruuRepository.findAllByUsuarioOrderByDataCriacaoDesc(usuario);
	}

	public List<Pruu> findAllOrderByCreatedAtDesc() {
		return pruuRepository.findAllByOrderByDataCriacaoDesc();
	}

	public void bloquear(UUID id) throws OpomboException {
		Pruu pruu = pruuRepository.findById(id).orElseThrow(() -> new OpomboException("Pruu não encontrado"));

		pruu.setBloqueado(true);

		pruuRepository.save(pruu);
	}

}

package com.victoria.pombo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import com.victoria.pombo.exception.OpomboException;
import com.victoria.pombo.model.dto.PruuDTO;
import com.victoria.pombo.model.entity.Denuncia;
import com.victoria.pombo.model.entity.Pruu;
import com.victoria.pombo.model.entity.Usuario;
import com.victoria.pombo.model.repository.PruuRepository;
import com.victoria.pombo.model.repository.UsuarioRepository;
import com.victoria.pombo.model.seletor.PruuSeletor;

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

	public Optional<Pruu> pesquisarPorId(String id) throws OpomboException {
		Optional<Pruu> pruu = pruuRepository.findById(id);
		if (pruu.isPresent()) {
			return pruu;
		} else {
			throw new OpomboException("Pruu  ID " + id + " não encontrado.");
		}
	}

	public void excluir(String id) {
		pruuRepository.deleteById(id);
	}

	public Pruu curtirPruu(String pruuId, Integer usuarioId) throws OpomboException {
		Pruu pruu = pruuRepository.findById(pruuId).orElseThrow(() -> new OpomboException("Pruu não encontrado"));

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

	public void bloquear(String id) throws OpomboException {
		Pruu pruu = pruuRepository.findById(id).orElseThrow(() -> new OpomboException("Pruu não encontrado"));

		pruu.setBloqueado(true);

		pruuRepository.save(pruu);
	}

	public List<Pruu> pesquisarComFiltros(PruuSeletor seletor) {

		if (!seletor.temFiltro()) {
			// Se não houver filtros, retorna todos os registros
			return pruuRepository.findAll();
		}

		// Usa o Specification para aplicar os filtros do seletor
		Specification<Pruu> specification = seletor;

		// Executa a consulta com os filtros
		return pruuRepository.findAll(specification);
	}

	public List<Usuario> qtdCurtidas(String publicationId) throws OpomboException {
		Pruu pruu = pruuRepository.findById(publicationId)
				.orElseThrow(() -> new OpomboException("Publicação não encontrada."));

		return pruu.getLikes();
	}

	public List<Denuncia> qtdDenuncias(String pruuID) throws OpomboException {
		Pruu pruu = pruuRepository.findById(pruuID)
				.orElseThrow(() -> new OpomboException("Publicação não encontrada."));

		return pruu.getDenuncias();
	}

	public List<PruuDTO> gerarDTO() throws OpomboException {
		List<Pruu> pruus = this.pesquisarTodos();
		List<PruuDTO> dto = new ArrayList<>();

		for (Pruu pruu : pruus) {
			Integer qtdLikes = this.qtdCurtidas(pruu.getUuid()).size();
			Integer qtdDenuncias = this.qtdDenuncias(pruu.getUuid()).size();
			PruuDTO pruuDTO = Pruu.toDTO(pruu, qtdLikes, qtdDenuncias);
			dto.add(pruuDTO);
		}

		return dto;

	}
}

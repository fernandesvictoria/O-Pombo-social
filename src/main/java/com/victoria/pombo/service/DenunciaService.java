package com.victoria.pombo.service;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;

import com.victoria.pombo.exception.OpomboException;
import com.victoria.pombo.model.dto.DenunciaDTO;
import com.victoria.pombo.model.entity.Denuncia;
import com.victoria.pombo.model.entity.Motivo;
import com.victoria.pombo.model.entity.Usuario;
import com.victoria.pombo.model.repository.DenunciaRepository;
import com.victoria.pombo.model.repository.UsuarioRepository;
import com.victoria.pombo.model.seletor.DenunciaSeletor;

public class DenunciaService {

	@Autowired
	private DenunciaRepository denunciaRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	public Denuncia inserir(Denuncia denuncia) throws OpomboException {
		if (denuncia.getMotivo() == null || !EnumSet.allOf(Motivo.class).contains(denuncia.getMotivo())) {
			throw new OpomboException("Motivo inválido");
		}

		boolean denunciaExistente = denunciaRepository.existsByUsuarioIdAndPostagemId(denuncia.getUser().getId(),
				denuncia.getPruu().getUuid());

		if (denunciaExistente) {
			throw new OpomboException("Usuário já denunciou esta postagem.");
		}

		return denunciaRepository.save(denuncia);
	}

	public List<Denuncia> pesquisarTodos(Integer userId) throws OpomboException {
		isAdmin(userId);
		return denunciaRepository.findAll();
	}

	public Denuncia pesquisarPorID(String complaintId, Integer userId) throws OpomboException {
		isAdmin(userId);
		return denunciaRepository.findById(complaintId).orElseThrow(() -> new OpomboException("Denúncia não existe."));
	}

	public void isAdmin(Integer userId) throws OpomboException {
		Usuario usuario = usuarioRepository.findById(userId)
				.orElseThrow(() -> new OpomboException("Usuário não encontrado."));

		if (!usuario.isAdmin()) {
			throw new OpomboException("Usuário não autorizado.");
		}
	}

	public void atualizarSituacaoDenuncia(String idDenuncia) throws OpomboException {
		Denuncia denuncia = this.denunciaRepository.findById(idDenuncia)
				.orElseThrow(() -> new OpomboException("Denúncia não existe."));

		denuncia.setAnalisada(!denuncia.getAnalisada());

		this.denunciaRepository.save(denuncia);
	}

	public boolean excluir(String id, String usuarioAtualId) throws OpomboException {
		Optional<Denuncia> denunciaOpt = denunciaRepository.findById(id);
		if (denunciaOpt.isPresent()) {
			Denuncia denuncia = denunciaOpt.get();

			if (denuncia.getId().equals(usuarioAtualId)) {
				denunciaRepository.deleteById(id);
				return true;
			} else {
				throw new OpomboException("Apenas o usuário que realizou a denúncia pode deletá-la.");
			}
		} else {
			throw new OpomboException("Denúncia não encontrada.");
		}
	}

	public List<Denuncia> pesquisarComFiltros(DenunciaSeletor seletor, Integer userId) throws OpomboException {
		isAdmin(userId);

		return denunciaRepository.findAll(seletor, Sort.by(Sort.Direction.DESC));
	}

	public DenunciaDTO gerarDTO(Integer usuarioID, String pruuID) throws OpomboException {
		isAdmin(usuarioID); // Verifica se o usuário é administrador
		List<Denuncia> denuncias = this.denunciaRepository.pesquisarPruuPorID(pruuID);
		List<Denuncia> denunciasPendentes = new ArrayList<>();
		List<Denuncia> denunciaAnalisadas = new ArrayList<>();

		for (Denuncia d : denuncias) {
			if (!d.getAnalisada()) { // Se 'analisada' for false, significa que está pendente
				denunciasPendentes.add(d);
			}
			if (d.getAnalisada()) { // Se 'analisada' for true, significa que já foi analisada
				denunciaAnalisadas.add(d);
			}
		}

		DenunciaDTO dto = Denuncia.toDTO(pruuID, denuncias.size(), denunciasPendentes.size(),
				denunciaAnalisadas.size());
		return dto;
	}

}

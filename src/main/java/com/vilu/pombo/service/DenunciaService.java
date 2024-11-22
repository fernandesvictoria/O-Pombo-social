package com.vilu.pombo.service;

import com.vilu.pombo.exeption.PomboException;
import com.vilu.pombo.model.dto.DenunciaDTO;
import com.vilu.pombo.model.entity.Denuncia;
import com.vilu.pombo.model.repository.DenunciaRepository;
import com.vilu.pombo.model.repository.PruuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DenunciaService {

    @Autowired
    private DenunciaRepository denunciaRepository;

    @Autowired
    private PruuRepository pruuRepository;

    public Denuncia denunciar(Denuncia denuncia) throws PomboException {
        boolean usuarioJaDenunciou = denunciaRepository.findAll()
                .stream()
                .anyMatch(existingDenuncia ->
                        Objects.equals(existingDenuncia.getUsuarioId(), denuncia.getUsuarioId()) &&
                                Objects.equals(existingDenuncia.getPruuId(), denuncia.getPruuId()));

        if (usuarioJaDenunciou) {
            throw new PomboException("O usuário já realizou uma denúncia para este Pruu.");
        }

        return denunciaRepository.save(denuncia);
    }

    public List<Denuncia> listarDenuncias() {
        return denunciaRepository.findAll();
    }

    public Optional<Denuncia> pesquisarDenunciaPorId(String idDenuncia) {
        return denunciaRepository.findById(idDenuncia);
    }

    public List<Denuncia> listarDenunciasPorIdPruu(String idPruu) {
        return denunciaRepository.findAll()
                .stream()
                .filter(denuncia -> Objects.equals(denuncia.getPruuId(), idPruu))
                .collect(Collectors.toList());
    }

    public DenunciaDTO gerarRelatorioPorIdPruu(String idPruu) {
        long quantidadeDenuncias = denunciaRepository.findAll()
                .stream()
                .filter(denuncia -> Objects.equals(denuncia.getPruuId(), idPruu))
                .count();

        return DenunciaDTO.builder()
                .idPruu(idPruu)
                .quantidadeDenuncias((int) quantidadeDenuncias)
                .build();
    }
}

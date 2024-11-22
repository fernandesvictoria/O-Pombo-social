package com.vilu.pombo.service;

import com.vilu.pombo.exeption.PomboException;
import com.vilu.pombo.model.dto.PruuDTO;
import com.vilu.pombo.model.entity.Denuncia;
import com.vilu.pombo.model.entity.Pruu;
import com.vilu.pombo.model.entity.Usuario;
import com.vilu.pombo.model.repository.PruuRepository;
import com.vilu.pombo.model.repository.UsuarioRepository;
import com.vilu.pombo.model.seletor.PruuSeletor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class PruuService {

    @Autowired
    private PruuRepository pruuRepository;

    @Autowired
    private DenunciaService denunciaService;

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private ImagemService imagemService;

    public Pruu inserir(Pruu pruu) throws PomboException {
        Usuario usuario = usuarioRepository.findById(pruu.getUsuario().getUuid()).orElseThrow(() -> new PomboException("Usuário não encontrado."));
        usuario.getPruus().add(pruu);
        return pruuRepository.save(pruu);
    }

    public List<Pruu> listarTodos() {
        return pruuRepository.findAll();
    }

    public Optional<Pruu> pesquisarPorId(String id) {
        return pruuRepository.findById(id);
    }

    public List<Pruu> pesquisarComSeletor(PruuSeletor seletor) {
        if (seletor.hasPaginacao()) {
            PageRequest pagina = PageRequest.of(seletor.getPagina() - 1, seletor.getLimite());
            return pruuRepository.findAll(seletor, pagina).toList();
        }
        return pruuRepository.findAll();
    }

    public List<Pruu> listarPruusPorIdUsuario(String idUsuario) throws PomboException {
        return usuarioRepository.findById(idUsuario).map(Usuario::getPruus).orElseThrow(() -> new PomboException("Usuário não encontrado."));
    }

    public Pruu atualizar(Pruu pruu) throws PomboException {
        if (!pruuRepository.existsById(pruu.getUuid())) {
            throw new PomboException("Pruu não encontrado.");
        }
        return pruuRepository.save(pruu);
    }

    public void excluir(String id) throws PomboException {
        Pruu pruu = pruuRepository.findById(id).orElseThrow(() -> new PomboException("Pruu não encontrado."));
        pruuRepository.delete(pruu);
    }

    public Pruu bloquear(String idPruu, String idUsuario) throws PomboException {
        Usuario usuario = usuarioRepository.findById(idUsuario).orElseThrow(() -> new PomboException("Usuário não encontrado."));

        if (!usuario.isAdmin()) {
            throw new PomboException("Usuário não é administrador.");
        }

        List<Denuncia> denuncias = denunciaService.listarDenunciasPorIdPruu(idPruu);
        if (denuncias.isEmpty()) {
            throw new PomboException("Nenhuma denúncia encontrada para este Pruu.");
        }

        Pruu pruu = pruuRepository.findById(idPruu).orElseThrow(() -> new PomboException("Pruu não encontrado."));

        pruu.setBloqueado(true);
        return pruuRepository.save(pruu);
    }

    public PruuDTO gerarRelatorioPruu(String idPruu) throws PomboException {
        Pruu pruu = pruuRepository.findById(idPruu).orElseThrow(() -> new PomboException("Pruu não encontrado."));

        List<Denuncia> denuncias = denunciaService.listarDenunciasPorIdPruu(pruu.getUuid());
        Usuario criador = pruu.getUsuario();

        return PruuDTO.builder().usuarioId(criador.getUuid()).nomeUsuario(criador.getNome()).quantidadeLikes(pruu.getQuantidadeLikes()).quantidadeDenuncias(denuncias.size()).texto(pruu.isBloqueado() ? "Este pruu foi bloqueado por violar os termos de uso." : pruu.getTexto()).build();
    }

    public void salvarImagemPruu(MultipartFile imagem, String idPruu) throws PomboException {

        Pruu pruuComNovaImagem = pruuRepository.findById(idPruu).orElseThrow(() -> new PomboException("Pruu não encontrado"));

        //Converter a imagem para base64
        String imagemBase64 = imagemService.processarImagem(imagem);

        //Inserir a imagem na coluna imagemEmBase64 do pruu

        //TODO ajustar para fazer o upload
        pruuComNovaImagem.setImagemEmBase64(imagemBase64);

        //Chamar pruuRepository para persistir a imagem na pruu
        pruuRepository.save(pruuComNovaImagem);
    }
}

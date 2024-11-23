package com.vilu.pombo.model.repository;

import com.vilu.pombo.factory.DenunciaFactory;
import com.vilu.pombo.model.entity.Denuncia;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
public class DenunciaRepositoryTest {

    @Autowired
    private DenunciaRepository denunciaRepository;

    @BeforeEach
    void setUp() {
        Denuncia denuncia = DenunciaFactory.criarDenunciaDefault();
        denunciaRepository.save(denuncia);
    }

    @Test
    void deveSalvarDenunciaComSucesso() {
        Denuncia denuncia = DenunciaFactory.criarDenunciaComMotivoSpam("uuidPruuTest", "uuidUsuarioTest");

        Denuncia denunciaSalva = denunciaRepository.save(denuncia);

        assertNotNull(denunciaSalva.getUuid());
        assertEquals("uuidPruuTest", denunciaSalva.getPruuId());
        assertEquals("uuidUsuarioTest", denunciaSalva.getUsuarioId());
        assertEquals("SPAM", denunciaSalva.getMotivo().toString());
    }

    @Test
    void deveEncontrarDenunciaPorUuid() {
        Denuncia denuncia = DenunciaFactory.criarDenunciaDefault();
        denunciaRepository.save(denuncia);

        Denuncia denunciaEncontrada = denunciaRepository.findById(denuncia.getUuid()).orElse(null);

        assertNotNull(denunciaEncontrada);
        assertEquals(denuncia.getUuid(), denunciaEncontrada.getUuid());
    }
}

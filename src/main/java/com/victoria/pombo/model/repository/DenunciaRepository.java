package com.victoria.pombo.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.victoria.pombo.model.entity.Denuncia;

@Repository
public interface DenunciaRepository extends JpaRepository<Denuncia, String>, JpaSpecificationExecutor<Denuncia> {
	
	
	List<Denuncia> findByPruuUuid(String uuid);
//arrumar
//	boolean existsByUsuarioIdAndPostagemId(Integer usuario_id, String pruuUuid);
}

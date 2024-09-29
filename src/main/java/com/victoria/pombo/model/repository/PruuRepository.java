package com.victoria.pombo.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import com.victoria.pombo.model.entity.Pruu;
import com.victoria.pombo.model.entity.Usuario;
import java.util.List;

@Repository
public interface PruuRepository extends JpaRepository<Pruu, String>, JpaSpecificationExecutor<Pruu> {

	List<Pruu> findAllByUsuarioOrderByDataCriacaoDesc(Usuario usuario);

	List<Pruu> findAllByOrderByDataCriacaoDesc();
}

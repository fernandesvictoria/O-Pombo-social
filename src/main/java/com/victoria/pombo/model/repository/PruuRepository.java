package com.victoria.pombo.model.repository;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.victoria.pombo.model.entity.Pruu;
import com.victoria.pombo.model.entity.Usuario;

import java.util.List;



@Repository
public interface PruuRepository extends JpaRepository<Pruu, UUID> {
	
	
	 List<Pruu> findAllByUsuarioOrderByDataCriacaoDesc(Usuario usuario);
	 
	 List<Pruu> findAllByOrderByDataCriacaoDesc();
}

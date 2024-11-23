package com.vilu.pombo.model.repository;

import com.vilu.pombo.model.entity.Denuncia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DenunciaRepository extends JpaRepository<Denuncia, String> {

}
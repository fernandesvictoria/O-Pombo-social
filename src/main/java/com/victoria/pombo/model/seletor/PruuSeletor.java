package com.victoria.pombo.model.seletor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.victoria.pombo.model.entity.Pruu;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Data;



@Data
public class PruuSeletor extends BaseSeletor implements Specification<Pruu>, Serializable {

    

    private String usuario;
    private LocalDateTime dataCriacao;

    public boolean temFiltro() {
        return (this.usuario != null && this.usuario.trim().length() > 0)
                || (this.dataCriacao != null);
    }

    @Override
    public Predicate toPredicate(Root<Pruu> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        if (this.usuario != null && this.usuario.trim().length() > 0) {
            predicates.add(cb.like(cb.lower(root.get("usuario")), "%" + this.usuario.toLowerCase() + "%"));
        }

        if (this.dataCriacao != null) {
            predicates.add(cb.equal(root.get("dataCriacao"), this.dataCriacao));
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}

package com.vilu.pombo.model.seletor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.vilu.pombo.model.entity.Usuario;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UsuarioSeletor extends BaseSeletor implements Specification<Usuario> {

    private String nome;

    public String getNome() {
		return nome;
	}

	public boolean temFiltro() {
        return (this.isValid(this.nome));
    }

    @Override
    public Predicate toPredicate(Root<Usuario> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        if (this.isValid(this.nome)) {
            predicates.add(cb.like(root.get("nome"), "%" + this.getNome() + "%"));
        }
        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
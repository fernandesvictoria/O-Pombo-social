package com.victoria.pombo.model.seletor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.victoria.pombo.model.entity.Usuario;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Data;



@Data
public class UsuarioSeletor extends BaseSeletor implements Specification<Usuario> {

	private String nome;
	private String email;

	
	public boolean temFiltro() {
		return (this.nome != null && this.nome.trim().length() > 0)
				|| (this.email != null && this.email.trim().length() > 0);
	}

	
	@Override
	public Predicate toPredicate(Root<Usuario> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
		List<Predicate> predicates = new ArrayList<>();

		// Filtro por nome (LIKE com qualquer parte do nome)
		if (this.nome != null && this.nome.trim().length() > 0) {
			predicates.add(cb.like(cb.lower(root.get("nome")), "%" + this.nome.toLowerCase() + "%"));
		}

		// Filtro por email (LIKE com qualquer parte do email)
		if (this.email != null && this.email.trim().length() > 0) {
			predicates.add(cb.like(cb.lower(root.get("email")), "%" + this.email.toLowerCase() + "%"));
		}

		return cb.and(predicates.toArray(new Predicate[0]));
	}
}

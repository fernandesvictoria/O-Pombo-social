package com.vilu.pombo.model.specification;

import com.vilu.pombo.model.entity.Usuario;
import com.vilu.pombo.model.seletor.UsuarioSeletor;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class UsuarioSpecification {

    public static Specification<Usuario> comFiltros(UsuarioSeletor seletor) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (seletor.getNome() != null && !seletor.getNome().trim().isEmpty()) {
                predicates.add(cb.like(root.get("nome"), "%" + seletor.getNome() + "%"));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

}
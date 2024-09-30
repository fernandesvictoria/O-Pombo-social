package com.victoria.pombo.model.seletor;


import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Data;

@Data
public abstract class BaseSeletor {
	private int pagina;
	private int limite;

	public BaseSeletor() {
		this.limite = 0;
		this.pagina = 0;
	}
 
	public boolean temPaginacao() {
		return this.limite > 0 && this.pagina > 0;
	}

	public boolean stringValida(String texto) {
		return texto != null && !texto.isBlank();
	}

	public static void aplicarFiltroPeriodo(Root<?> root, CriteriaBuilder cb, List<Predicate> predicates,
			LocalDate dataInicial, LocalDate dataFinal, String string) {
		
		if (dataInicial != null && dataFinal != null) {
			predicates.add(cb.between(root.get(string), dataInicial, dataFinal));
		} else if (dataInicial != null) {
			predicates.add(cb.greaterThanOrEqualTo(root.get(string), dataInicial));
		} else if (dataFinal != null) {
			predicates.add(cb.lessThanOrEqualTo(root.get(string), dataFinal));
		}
	}
}
package com.victoria.pombo.model.seletor;

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
}
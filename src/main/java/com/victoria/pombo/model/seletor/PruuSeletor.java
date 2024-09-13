package com.victoria.pombo.model.seletor;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class PruuSeletor extends BaseSeletor {

	private String usuario;
	private LocalDateTime dataCriacao;
}

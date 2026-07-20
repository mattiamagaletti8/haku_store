package com.betacom.jpa.dto.input;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// Proprietario: Mattia
@Setter
@Getter
@ToString
public class ProdottoReq {
	@NotNull(groups = ValidationGroups.Update.class, message = "prodotto.no.id")
	private Integer id;

	@NotNull(groups = ValidationGroups.Create.class, message = "prodotto.no.categoria")
	private Integer idCategoria;

	@NotNull(groups = ValidationGroups.Create.class, message = "prodotto.no.nome")
	@NotBlank(groups = ValidationGroups.Create.class, message = "prodotto.no.nome")
	private String nome;

	private String descrizione;

	@NotNull(groups = ValidationGroups.Create.class, message = "prodotto.no.marca")
	@NotBlank(groups = ValidationGroups.Create.class, message = "prodotto.no.marca")
	private String marca;
}

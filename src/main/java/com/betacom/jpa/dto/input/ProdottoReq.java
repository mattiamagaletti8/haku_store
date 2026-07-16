package com.betacom.jpa.dto.input;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// ============================================================================
// PROPRIETARIO: Mattia — Modulo Catalogo (Categoria / Prodotto / VarianteProdotto)
// ============================================================================
@Setter
@Getter
@ToString
public class ProdottoReq {
	// Obbligatorio solo in update, per sapere quale prodotto modificare
	@NotNull(groups = ValidationGroups.Update.class, message = "prodotto.no.id")
	private Integer id;

	// Obbligatorio solo in creazione: un prodotto nuovo deve nascere gia' agganciato a una categoria
	@NotNull(groups = ValidationGroups.Create.class, message = "prodotto.no.categoria")
	private Integer idCategoria;

	@NotNull(groups = ValidationGroups.Create.class, message = "prodotto.no.nome")
	@NotBlank(groups = ValidationGroups.Create.class, message = "prodotto.no.nome")
	private String nome;

	// Nessuna validazione: la descrizione e' sempre facoltativa, anche in creazione
	private String descrizione;

	@NotNull(groups = ValidationGroups.Create.class, message = "prodotto.no.marca")
	@NotBlank(groups = ValidationGroups.Create.class, message = "prodotto.no.marca")
	private String marca;
}

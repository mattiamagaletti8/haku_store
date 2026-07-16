package com.betacom.jpa.dto.input;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
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
public class VarianteProdottoReq {
	@NotNull(groups = ValidationGroups.Update.class, message = "variante.no.id")
	private Integer id;

	// Obbligatorio solo in creazione: ogni variante nuova deve nascere agganciata a un prodotto
	@NotNull(groups = ValidationGroups.Create.class, message = "variante.no.prodotto")
	private Integer idProdotto;

	// Nessuna validazione: sono attributi opzionali, dipende dalla categoria del prodotto quali servono
	private String gusto;
	private String formato;
	private String colore;

	@NotNull(groups = ValidationGroups.Create.class, message = "variante.no.prezzo")
	// DecimalMin("0.0") vale sia in Create che in Update: il prezzo non puo' mai essere negativo
	@DecimalMin(value = "0.0", groups = { ValidationGroups.Create.class, ValidationGroups.Update.class }, message = "variante.prezzo.invalid")
	private BigDecimal prezzo;

	// Non e' @NotNull: se non passata in creazione, il service la tratta come 0 (nessuno stock iniziale)
	@Min(value = 0, groups = { ValidationGroups.Create.class, ValidationGroups.Update.class }, message = "variante.quantita.invalid")
	private Integer quantitaDisponibile;
}

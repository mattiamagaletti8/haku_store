package com.betacom.jpa.dto.input;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// ============================================================================
// PROPRIETARIO: Pier — Modulo Carrello (Carrello / DettaglioCarrello / Coupon)
// ============================================================================
// Corpo delle richieste "aggiungi/modifica riga nel carrello" (POST/PATCH /rest/carrello/items)
@Setter
@Getter
@ToString
public class DettaglioCarrelloReq {
	// Entrambi i campi sono obbligatori sia in Create che in Update: non ha senso una riga
	// senza sapere quale variante e quanta quantita'
	@NotNull(groups = { ValidationGroups.Create.class, ValidationGroups.Update.class }, message = "carrello.no.variante")
	private Integer idVariante;

	@NotNull(groups = { ValidationGroups.Create.class, ValidationGroups.Update.class }, message = "carrello.no.quantita")
	// Min(1): niente righe con quantita' zero o negativa (per rimuovere una riga c'e' l'endpoint DELETE dedicato)
	@Min(value = 1, groups = { ValidationGroups.Create.class, ValidationGroups.Update.class }, message = "carrello.quantita.invalid")
	private Integer quantita;
}

package com.betacom.jpa.dto.input;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// ============================================================================
// PROPRIETARIO: Sarah — Modulo Utente, Recensioni & Sicurezza
// ============================================================================
// Nessun campo idUtente qui: l'autore della recensione deriva sempre dal principal autenticato
@Setter
@Getter
@ToString
public class RecensioneReq {
	@NotNull(groups = ValidationGroups.Update.class, message = "recensione.no.id")
	private Integer id;

	// Obbligatorio solo in creazione: una recensione nuova deve nascere agganciata a un prodotto
	@NotNull(groups = ValidationGroups.Create.class, message = "recensione.no.prodotto")
	private Integer idProdotto;

	@NotNull(groups = ValidationGroups.Create.class, message = "recensione.no.voto")
	// Range 1-5 valido sia in creazione che in modifica
	@Min(value = 1, groups = { ValidationGroups.Create.class, ValidationGroups.Update.class }, message = "recensione.voto.invalid")
	@Max(value = 5, groups = { ValidationGroups.Create.class, ValidationGroups.Update.class }, message = "recensione.voto.invalid")
	private Integer voto;

	// Entrambi facoltativi: si puo' lasciare solo un voto, senza testo
	private String titolo;
	private String commento;
}

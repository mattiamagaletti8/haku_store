package com.betacom.jpa.dto.input;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// ============================================================================
// PROPRIETARIO: Sarah — Modulo Utente, Recensioni & Sicurezza
// ============================================================================
// Nota: nessun campo idUtente qui — chi possiede l'indirizzo si stabilisce sempre
// lato server dal principal autenticato, mai da un valore passato dal client
@Setter
@Getter
@ToString
public class IndirizzoReq {
	@NotNull(groups = ValidationGroups.Update.class, message = "indirizzo.no.id")
	private Integer id;

	@NotNull(groups = ValidationGroups.Create.class, message = "indirizzo.no.via")
	@NotBlank(groups = ValidationGroups.Create.class, message = "indirizzo.no.via")
	private String via;

	@NotNull(groups = ValidationGroups.Create.class, message = "indirizzo.no.citta")
	@NotBlank(groups = ValidationGroups.Create.class, message = "indirizzo.no.citta")
	private String citta;

	@NotNull(groups = ValidationGroups.Create.class, message = "indirizzo.no.cap")
	@NotBlank(groups = ValidationGroups.Create.class, message = "indirizzo.no.cap")
	private String cap;

	// Facoltativa anche in creazione: dipende dal paese
	private String provincia;

	// Facoltativa: se non passata, il service la imposta di default a "Italia" (vedi IndirizzoImpl.create)
	private String nazione;
}

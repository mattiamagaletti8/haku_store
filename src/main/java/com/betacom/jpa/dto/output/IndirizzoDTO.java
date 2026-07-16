package com.betacom.jpa.dto.output;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

// ============================================================================
// PROPRIETARIO: Sarah — Modulo Utente, Recensioni & Sicurezza
// ============================================================================
// Nessun riferimento all'utente proprietario: quando si legge la lista indirizzi di un utente,
// e' gia' implicito di chi sono, non serve ripeterlo in ogni elemento
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class IndirizzoDTO {
	private Integer id;
	private String via;
	private String citta;
	private String cap;
	private String provincia;
	private String nazione;
}

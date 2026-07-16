package com.betacom.jpa.dto.output;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

// ============================================================================
// PROPRIETARIO: Sarah — Modulo Utente, Recensioni & Sicurezza
// ============================================================================
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RecensioneDTO {
	private Integer id;
	private Integer idProdotto;
	private Integer idUtente;
	// nome/cognome dell'autore appiattiti qui (non uno UtenteDTO annidato): la lista recensioni
	// e' pubblica, mostrare solo nome e cognome evita di esporre email/telefono dell'autore
	private String nomeUtente;
	private String cognomeUtente;
	private Integer voto;
	private String titolo;
	private String commento;
	private LocalDateTime dataRecensione;
}

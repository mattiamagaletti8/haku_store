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
// Risposta di login/register: il token che il frontend deve salvare e riallegare
// come header "Authorization: Bearer ..." su ogni chiamata successiva
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AuthResponseDTO {
	private String token;
	// Sempre "Bearer": informa il frontend di come comporre l'header Authorization
	private String tokenType;
	// Durata in secondi, utile al frontend per sapere quando il token scadra' e va rinnovato
	private long expiresIn;
	// Il profilo dell'utente appena autenticato, cosi' il frontend non deve fare una seconda chiamata
	private UtenteDTO utente;
}

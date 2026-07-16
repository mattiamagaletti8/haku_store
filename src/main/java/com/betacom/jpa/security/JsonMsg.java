package com.betacom.jpa.security;

// ============================================================================
// PROPRIETARIO: Sarah — Modulo Utente, Recensioni & Sicurezza
// ============================================================================
/**
 * Serializza a mano il corpo {"msg": "..."} (stessa forma di ResponseDTO) per i punti
 * della security filter chain che rispondono prima che il container Spring MVC/Jackson entri in gioco.
 */
// Package-private e final: un helper minuscolo con un solo scopo, usato solo da
// ApiAuthEntryPoint e ApiAccessDeniedHandler dentro questo stesso package
final class JsonMsg {

	// Costruttore privato: classe di soli metodi statici, non va istanziata
	private JsonMsg() {
	}

	static String responseDTOJson(String msg) {
		return "{\"msg\":\"" + escape(msg) + "\"}";
	}

	// Sostituisce backslash e virgolette per non rompere il JSON se il messaggio contiene
	// caratteri speciali: una piccola ma necessaria misura difensiva, dato che qui non c'e' Jackson
	// a occuparsi dell'escaping automaticamente
	private static String escape(String s) {
		if (s == null)
			return "";
		return s.replace("\\", "\\\\").replace("\"", "\\\"");
	}
}

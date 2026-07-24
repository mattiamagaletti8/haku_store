package com.betacom.jpa.security;

// Proprietario: Sara e Mattia
// classe di utilità per costruire a mano una piccola risposta JSON con un messaggio
final class JsonMsg {

	// costruttore privato: nessuno deve creare un oggetto, si usano solo i metodi static
	private JsonMsg() {
	}

	static String responseDTOJson(String msg) {
		// costruisce una stringa tipo {"msg":"testo"}
		return "{\"msg\":\"" + escape(msg) + "\"}";
	}

	private static String escape(String s) {
		if (s == null)
			return "";  // se non c'è messaggio, restituisce stringa vuota
		return s.replace("\\", "\\\\").replace("\"", "\\\""); // protegge i caratteri speciali per non rompere il JSON
	}
}

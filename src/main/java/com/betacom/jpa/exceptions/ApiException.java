package com.betacom.jpa.exceptions;

// ============================================================================
// PROPRIETARIO: Infrastruttura condivisa (non appartiene a una sola persona)
// ============================================================================
// La classe piu' piccola e piu' usata di tutto il progetto. Il trucco e' cosa ci si mette dentro:
// "message" non e' un testo per l'utente, e' un CODICE (es. "variante.stock.insufficient") che
// ExceptionManager risolve in un messaggio vero tramite IMessaggioServices.get(...).
// extends RuntimeException (non Exception): unchecked, cosi' non obbliga ogni metodo del backend
// a dichiararla con "throws ApiException".
public class ApiException extends RuntimeException {

	// Costruttore vuoto, quasi mai usato
	public ApiException() {
		super();
	}

	// Il costruttore usato ovunque nel backend: message e' sempre un codice messaggio, non testo libero
	public ApiException(String message) {
		super(message);
	}

}

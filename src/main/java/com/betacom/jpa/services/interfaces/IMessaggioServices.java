package com.betacom.jpa.services.interfaces;

// ============================================================================
// PROPRIETARIO: Infrastruttura condivisa (non appartiene a una sola persona)
// ============================================================================
// Un'unica operazione: dato un codice (es. "utente.email.exists"), restituisce il testo tradotto.
// Usata da ExceptionManager, ApiAuthEntryPoint e ApiAccessDeniedHandler per trasformare i codici
// di errore in messaggi leggibili, sempre nella stessa lingua configurata (application.properties: lang=IT)
public interface IMessaggioServices {
	String get(String code);
}

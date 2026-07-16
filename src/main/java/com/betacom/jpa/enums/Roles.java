package com.betacom.jpa.enums;

// ============================================================================
// PROPRIETARIO: Sarah — Modulo Utente, Recensioni & Sicurezza
// ============================================================================
// I due ruoli del sistema. Nessun endpoint speciale per creare il primo ADMIN:
// ci si registra come CLIENTE normale, poi si aggiorna manualmente il campo a mano via SQL.
public enum Roles {
	// Ruolo di default per ogni nuova registrazione (vedi AuthImpl.register)
	CLIENTE,
	// Sblocca @PreAuthorize("hasRole('ADMIN')") su tutti i controller di mutazione del catalogo/coupon/ordini
	ADMIN
}

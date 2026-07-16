package com.betacom.jpa.services.interfaces;

import com.betacom.jpa.dto.input.LoginReq;
import com.betacom.jpa.dto.input.UtenteReq;
import com.betacom.jpa.dto.output.AuthResponseDTO;

// ============================================================================
// PROPRIETARIO: Sarah — Modulo Utente, Recensioni & Sicurezza
// ============================================================================
public interface IAuthServices {
	// Registra un nuovo utente ED emette subito un token: login automatico dopo la registrazione,
	// senza costringere l'utente a fare login separatamente
	AuthResponseDTO register(UtenteReq req) throws Exception;

	AuthResponseDTO login(LoginReq req) throws Exception;
}

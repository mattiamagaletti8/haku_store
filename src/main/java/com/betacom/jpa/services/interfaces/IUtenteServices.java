package com.betacom.jpa.services.interfaces;

import java.util.List;

import com.betacom.jpa.dto.input.UtenteReq;
import com.betacom.jpa.dto.output.UtenteDTO;
import com.betacom.jpa.models.Utente;

// ============================================================================
// PROPRIETARIO: Sarah — Modulo Utente, Recensioni & Sicurezza
// ============================================================================
public interface IUtenteServices {
	/**
	 * Self-registrazione: forza sempre ruolo CLIENTE, indipendentemente da req.getRuolo().
	 */
	UtenteDTO create(UtenteReq req) throws Exception;

	// callerId/isAdmin: pattern di ownership manuale, un utente puo' modificare solo se stesso
	// a meno che il chiamante non sia ADMIN
	void update(UtenteReq req, Integer callerId, boolean isAdmin) throws Exception;

	void delete(Integer id, Integer callerId, boolean isAdmin) throws Exception;

	// Solo ADMIN (controllato nel controller): lista di tutti gli utenti registrati
	List<UtenteDTO> list() throws Exception;

	UtenteDTO getById(Integer id, Integer callerId, boolean isAdmin) throws Exception;

	/**
	 * Usato dal livello di autenticazione (login/JwtAuthFilter): espone l'entita' completa (con password hash).
	 */
	// Deliberatamente distinto da getById: questo restituisce l'entity GREZZA (con l'hash password),
	// necessaria per il confronto password durante il login — non deve mai transitare verso il frontend
	Utente getEntityByEmail(String email) throws Exception;
}

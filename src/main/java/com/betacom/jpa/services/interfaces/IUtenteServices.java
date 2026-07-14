package com.betacom.jpa.services.interfaces;

import java.util.List;

import com.betacom.jpa.dto.input.UtenteReq;
import com.betacom.jpa.dto.output.UtenteDTO;
import com.betacom.jpa.models.Utente;

public interface IUtenteServices {
	/**
	 * Self-registrazione: forza sempre ruolo CLIENTE, indipendentemente da req.getRuolo().
	 */
	UtenteDTO create(UtenteReq req) throws Exception;

	void update(UtenteReq req, Integer callerId, boolean isAdmin) throws Exception;

	void delete(Integer id, Integer callerId, boolean isAdmin) throws Exception;

	List<UtenteDTO> list() throws Exception;

	UtenteDTO getById(Integer id, Integer callerId, boolean isAdmin) throws Exception;

	/**
	 * Usato dal livello di autenticazione (login/JwtAuthFilter): espone l'entita' completa (con password hash).
	 */
	Utente getEntityByEmail(String email) throws Exception;
}

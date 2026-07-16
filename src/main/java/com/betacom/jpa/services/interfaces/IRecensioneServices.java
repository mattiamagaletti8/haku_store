package com.betacom.jpa.services.interfaces;

import java.util.List;

import com.betacom.jpa.dto.input.RecensioneReq;
import com.betacom.jpa.dto.output.RecensioneDTO;

// ============================================================================
// PROPRIETARIO: Sarah — Modulo Utente, Recensioni & Sicurezza
// ============================================================================
public interface IRecensioneServices {
	// idUtente separato da req: sempre dal principal autenticato, mai dal body
	void create(RecensioneReq req, Integer idUtente) throws Exception;

	void update(RecensioneReq req, Integer callerId, boolean isAdmin) throws Exception;

	void delete(Integer id, Integer callerId, boolean isAdmin) throws Exception;

	// Nessun controllo di ownership: la lista recensioni di un prodotto e' pubblica
	List<RecensioneDTO> listByProdotto(Integer idProdotto) throws Exception;
}
